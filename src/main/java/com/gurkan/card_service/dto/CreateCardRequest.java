package com.gurkan.card_service.dto;

import com.gurkan.card_service.enums.CardType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateCardRequest(
    @NotNull
    UUID customerId,
    @NotNull
    CardType cardType
) {
}
