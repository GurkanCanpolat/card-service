package com.gurkan.card_service.dto;

import com.gurkan.card_service.enums.TransactionType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record CreateTransactionRequest(
        @NotNull UUID cardId,
        @NotNull UUID requestId,

        @NotNull
        @DecimalMin(value = "0.01")
        BigDecimal amount,

        @NotNull String currency,
        @NotNull TransactionType type) {
}
