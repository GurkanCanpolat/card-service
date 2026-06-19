package com.gurkan.card_service.dto;

import com.gurkan.card_service.enums.CardStatus;
import com.gurkan.card_service.enums.CardType;

import java.time.Instant;
import java.util.UUID;

public record CardResponse(
        UUID id,
        UUID customerId,
        String cardNumber,
        CardType cardType,
        CardStatus status,
        Instant createdAt
) {
}
