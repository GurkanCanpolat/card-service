package com.gurkan.card_service.dto;

import com.gurkan.card_service.enums.TransactionStatus;
import com.gurkan.card_service.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionCreatedEvent(UUID transactionId,
                                      UUID requestId,
                                      UUID cardId,
                                      BigDecimal amount,
                                      String currency,
                                      TransactionType type,
                                      TransactionStatus status,
                                      Instant createdAt) {
}
