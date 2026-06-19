package com.gurkan.card_service.service;

import com.gurkan.card_service.dto.CreateTransactionRequest;
import com.gurkan.card_service.dto.TransactionResponse;
import com.gurkan.card_service.entity.Card;
import com.gurkan.card_service.entity.CardLimit;
import com.gurkan.card_service.entity.CardTransaction;
import com.gurkan.card_service.enums.CardStatus;
import com.gurkan.card_service.enums.CardType;
import com.gurkan.card_service.enums.TransactionStatus;
import com.gurkan.card_service.enums.TransactionType;
import com.gurkan.card_service.mapper.CardTransactionMapper;
import com.gurkan.card_service.repository.CardLimitRepository;
import com.gurkan.card_service.repository.CardRepository;
import com.gurkan.card_service.repository.CardTransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
public class CardTransactionServiceTest {
    @Mock
    private CardRepository cardRepository;

    @Mock
    private CardTransactionRepository transactionRepository;

    @Mock
    private CardLimitRepository cardLimitRepository;

    @Mock
    private CardTransactionMapper transactionMapper;

    @InjectMocks
    private CardTransactionService transactionService;

    @Test
    void shouldApproveTransactionAndDecreaseAvailableLimit() {
        UUID cardId = UUID.randomUUID();
        UUID requestId = UUID.randomUUID();

        Card card = Card.builder()
                .id(cardId)
                .customerId(UUID.randomUUID())
                .cardType(CardType.CREDIT)
                .status(CardStatus.ACTIVE)
                .build();

        CardLimit cardLimit = CardLimit.builder()
                .id(UUID.randomUUID())
                .cardId(cardId)
                .totalLimit(BigDecimal.valueOf(10_000))
                .availableLimit(BigDecimal.valueOf(10_000))
                .build();

        CreateTransactionRequest request = new CreateTransactionRequest(
                requestId,
                cardId,
                BigDecimal.valueOf(2_500),
                "TRY",
                TransactionType.PURCHASE
        );

        when(transactionRepository.findByRequestId(requestId))
                .thenReturn(Optional.empty());

        when(cardRepository.findById(cardId))
                .thenReturn(Optional.of(card));

        when(cardLimitRepository.findByCardId(cardId))
                .thenReturn(Optional.of(cardLimit));

        when(transactionRepository.save(any(CardTransaction.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(transactionMapper.toResponse(any(CardTransaction.class)))
                .thenAnswer(invocation -> {
                    CardTransaction transaction = invocation.getArgument(0);

                    return new TransactionResponse(
                            transaction.getId(),
                            transaction.getCardId(),
                            transaction.getOriginalTransactionId(),
                            transaction.getAmount(),
                            transaction.getCurrency(),
                            transaction.getType(),
                            transaction.getStatus(),
                            transaction.getCreatedAt()
                    );
                });

        TransactionResponse response =
                transactionService.createTransaction(request);

        assertThat(response.status()).isEqualTo(TransactionStatus.APPROVED);
        assertThat(cardLimit.getAvailableLimit())
                .isEqualByComparingTo(BigDecimal.valueOf(7_500));

        verify(cardRepository).findById(cardId);
        verify(cardLimitRepository).findByCardId(cardId);
        verify(transactionRepository).save(any(CardTransaction.class));
        verify(cardLimitRepository).save(cardLimit);
    }
}
