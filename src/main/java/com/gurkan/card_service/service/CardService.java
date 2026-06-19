package com.gurkan.card_service.service;

import com.gurkan.card_service.dto.CardResponse;
import com.gurkan.card_service.dto.CreateCardRequest;
import com.gurkan.card_service.dto.UpdateCardStatusRequest;
import com.gurkan.card_service.entity.Card;
import com.gurkan.card_service.entity.CardLimit;
import com.gurkan.card_service.entity.Customer;
import com.gurkan.card_service.enums.CardStatus;
import com.gurkan.card_service.exception.CardNotFoundException;
import com.gurkan.card_service.exception.CustomerNotFoundException;
import com.gurkan.card_service.exception.InvalidCardStatusTransitionException;
import com.gurkan.card_service.mapper.CardMapper;
import com.gurkan.card_service.repository.CardLimitRepository;
import com.gurkan.card_service.repository.CardRepository;
import com.gurkan.card_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CardService {
    private final CardRepository cardRepository;
    private final CustomerRepository customerRepository;
    private final CardLimitRepository cardLimitRepository;
    private final CardMapper cardMapper;

    public CardResponse createCard(CreateCardRequest request) {

        Customer customer =customerRepository.findById(request.customerId())
                .orElseThrow(() -> new CustomerNotFoundException(request.customerId()));


        Card card = Card.builder()
                .id(UUID.randomUUID())
                .customerId(request.customerId())
                .cardType(request.cardType())
                .cardNumber(generateMaskedCardNumber())
                .status(CardStatus.ACTIVE)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        Card savedCard = cardRepository.save(card);
        CardLimit cardLimit = CardLimit.builder()
                .id(UUID.randomUUID())
                .cardId(savedCard.getId())
                .totalLimit(BigDecimal.ZERO)
                .availableLimit(BigDecimal.ZERO)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        cardLimitRepository.save(cardLimit);
        return cardMapper.toResponse(savedCard);
    }

    public List<CardResponse> getCardsByCustomerId(UUID customerId) {
        Customer customer =customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return cardRepository.findByCustomerId(customerId).stream().map(cardMapper::toResponse).toList();
    }

    public CardResponse getCardById(UUID id) {
        Card card = cardRepository.findById(id)
                .orElseThrow(() -> new CardNotFoundException(id));

        return cardMapper.toResponse(card);
    }

    public List<CardResponse> getActiveCardsByCustomerId(UUID customerId) {
        Customer customer =customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return cardRepository.findActiveCardsByCustomerId(customerId, CardStatus.ACTIVE).stream()
                .map(cardMapper::toResponse)
                .toList();
    }

    public List<CardResponse> getCardsByCustomerIdAndStatuses(UUID customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        return cardRepository.findCardsByCustomerIdAndStatusActive(customerId).stream()
                .map(cardMapper::toResponse)
                .toList();
    }


    @Transactional
    public CardResponse updateCardStatus(UUID cardId, UpdateCardStatusRequest request) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new CardNotFoundException(cardId));

        validateStatusTransition(
                card.getStatus(),
                request.status()
        );
        card.setStatus(request.status());
        card.setUpdatedAt(Instant.now());
        Card updatedCard = cardRepository.save(card);
        return cardMapper.toResponse(updatedCard);
    }

    private String generateMaskedCardNumber() {
        return "5555 **** **** " + (int) (Math.random() * 9000 + 1000);
    }
    private void validateStatusTransition(
            CardStatus currentStatus,
            CardStatus targetStatus
    ) {

        if (currentStatus == CardStatus.CLOSED) {
            throw new InvalidCardStatusTransitionException(
                    currentStatus,
                    targetStatus
            );
        }
    }
}
