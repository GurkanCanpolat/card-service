package com.gurkan.card_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gurkan.card_service.dto.CreateTransactionRequest;
import com.gurkan.card_service.dto.TransactionCreatedEvent;
import com.gurkan.card_service.dto.TransactionResponse;
import com.gurkan.card_service.entity.CardLimit;
import com.gurkan.card_service.entity.CardTransaction;
import com.gurkan.card_service.entity.OutboxEvent;
import com.gurkan.card_service.enums.CardStatus;
import com.gurkan.card_service.enums.OutboxEventStatus;
import com.gurkan.card_service.enums.TransactionStatus;
import com.gurkan.card_service.enums.TransactionType;
import com.gurkan.card_service.exception.*;
import com.gurkan.card_service.mapper.CardTransactionMapper;
import com.gurkan.card_service.metrics.TransactionMetrics;
import com.gurkan.card_service.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardTransactionService {
    private final CardRepository cardRepository;
    private final CardTransactionMapper cardTransactionMapper;
    private final CardTransactionRepository cardTransactionRepository;
    private final CustomerRepository customerRepository;
    private final CardLimitRepository cardLimitRepository;
    private final OutboxEventRepository outboxEventRepository;
    private final ObjectMapper objectMapper;
    private final TransactionMetrics transactionMetrics;

    private static final BigDecimal DAILY_TRANSACTION_LIMIT = BigDecimal.valueOf(10_000);

    @Transactional
    public TransactionResponse createTransaction(CreateTransactionRequest createTransactionRequest){

        Optional<CardTransaction> existingCardTransaction = cardTransactionRepository.findByRequestId(createTransactionRequest.requestId());

        if(existingCardTransaction.isPresent()){
            return cardTransactionMapper.toResponse(existingCardTransaction.get());
        }
        var card = cardRepository.findById(createTransactionRequest.cardId())
                .orElseThrow(() -> new RuntimeException("Card not found with id: " + createTransactionRequest.cardId()));
        if(card.getStatus() != CardStatus.ACTIVE) {
            throw new CardNotActiveException(card.getId(), card.getStatus());
        }

        customerRepository.findById(card.getCustomerId())
                .orElseThrow(() -> new CustomerNotFoundException(card.getCustomerId()));

        CardLimit cardLimit = cardLimitRepository.findByCardId(card.getId())
                .orElseThrow(() -> new CardLimitNotFoundException(card.getId()));

        TransactionStatus transactionStatus =
                determineTransactionStatus(createTransactionRequest.amount(), cardLimit.getAvailableLimit());

        if (transactionStatus == TransactionStatus.APPROVED) {
            cardLimit.setAvailableLimit(
                    cardLimit.getAvailableLimit().subtract(createTransactionRequest.amount())
            );
            cardLimit.setUpdatedAt(Instant.now());
            cardLimitRepository.save(cardLimit);
            transactionMetrics.recordApproved();
        }
      else if (transactionStatus == TransactionStatus.DECLINED) {
            transactionMetrics.recordDeclined();
        }else {
            transactionMetrics.recordReversed();
        }

        CardTransaction transaction = CardTransaction.builder()
                .id(UUID.randomUUID())
                .cardId(createTransactionRequest.cardId())
                .amount(createTransactionRequest.amount())
                .currency(createTransactionRequest.currency())
                .type(createTransactionRequest.type())
                .status(determineTransactionStatus(createTransactionRequest.amount(), cardLimit.getAvailableLimit()))
                .createdAt(Instant.now())
                .requestId(createTransactionRequest.requestId())
                .build();
        var savedTransaction = cardTransactionRepository.save(transaction);

        createTransactionCreatedOutboxEvent(savedTransaction);
        return cardTransactionMapper.toResponse(savedTransaction);

    }
    public List<TransactionResponse> getTransactionsByCardId(UUID cardId) {
        cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        return cardTransactionRepository.findByCardId(cardId)
                .stream()
                .map(cardTransactionMapper::toResponse)
                .toList();
    }
    private TransactionStatus determineTransactionStatus(
            BigDecimal amount,
            BigDecimal availableLimit
    ) {
        if (amount.compareTo(availableLimit) > 0) {
            return TransactionStatus.DECLINED;
        }

        return TransactionStatus.APPROVED;
    }

    @Transactional
    public TransactionResponse reverseTransaction(UUID transactionId) {
        CardTransaction originalTransaction = cardTransactionRepository.findById(transactionId)
                .orElseThrow(() -> new InvalidTransactionReversalException(
                        transactionId,
                        "Original transaction not found"
                ));

        validateReversal(originalTransaction);

        CardLimit cardLimit = cardLimitRepository.findByCardId(originalTransaction.getCardId())
                .orElseThrow(() -> new CardLimitNotFoundException(originalTransaction.getCardId()));

        CardTransaction reversalTransaction = CardTransaction.builder()
                .id(UUID.randomUUID())
                .cardId(originalTransaction.getCardId())
                .originalTransactionId(originalTransaction.getId())
                .amount(originalTransaction.getAmount())
                .currency(originalTransaction.getCurrency())
                .type(TransactionType.REVERSAL)
                .status(TransactionStatus.APPROVED)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        originalTransaction.setStatus(TransactionStatus.REVERSED);
        originalTransaction.setUpdatedAt(Instant.now());

        cardLimit.setAvailableLimit(
                cardLimit.getAvailableLimit().add(originalTransaction.getAmount())
        );
        cardLimit.setUpdatedAt(Instant.now());

        cardTransactionRepository.save(originalTransaction);
        cardLimitRepository.save(cardLimit);

        CardTransaction savedReversalTransaction =
                cardTransactionRepository.save(reversalTransaction);

        return cardTransactionMapper.toResponse(savedReversalTransaction);
    }

    private void validateReversal(CardTransaction transaction) {
        if (transaction.getStatus() != TransactionStatus.APPROVED) {
            throw new InvalidTransactionReversalException(
                    transaction.getId(),
                    "Only APPROVED transactions can be reversed"
            );
        }

        if (transaction.getType() == TransactionType.REVERSAL) {
            throw new InvalidTransactionReversalException(
                    transaction.getId(),
                    "Reversal transaction cannot be reversed again"
            );
        }

        if (cardTransactionRepository.existsByOriginalTransactionId(transaction.getId())) {
            throw new InvalidTransactionReversalException(
                    transaction.getId(),
                    "Transaction has already been reversed"
            );
        }
    }

    private void createTransactionCreatedOutboxEvent(CardTransaction transaction) {
        TransactionCreatedEvent event = new TransactionCreatedEvent(
                transaction.getId(),
                transaction.getRequestId(),
                transaction.getCardId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getType(),
                transaction.getStatus(),
                transaction.getCreatedAt()
        );

        try {
            OutboxEvent outboxEvent = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("CARD_TRANSACTION")
                    .aggregateId(transaction.getId())
                    .eventType("TRANSACTION_CREATED")
                    .payload(objectMapper.writeValueAsString(event))
                    .status(OutboxEventStatus.PENDING)
                    .createdAt(Instant.now())
                    .build();

            outboxEventRepository.save(outboxEvent);

        } catch (JsonProcessingException exception) {
            throw new RuntimeException("Failed to serialize transaction event", exception);
        }
    }
}
