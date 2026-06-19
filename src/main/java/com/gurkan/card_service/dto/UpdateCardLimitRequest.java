package com.gurkan.card_service.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record UpdateCardLimitRequest(@NotNull
                                             @DecimalMin(value = "0.00")
                                     BigDecimal totalLimit) {
}
