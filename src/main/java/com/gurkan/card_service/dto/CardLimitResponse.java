package com.gurkan.card_service.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record CardLimitResponse(UUID cardId,
                                BigDecimal totalLimit,
                                BigDecimal availableLimit,
                                Instant updatedAt) {
}
