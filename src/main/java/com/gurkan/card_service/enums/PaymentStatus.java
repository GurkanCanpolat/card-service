package com.gurkan.card_service.enums;

public enum PaymentStatus {
    INITIATED,
    VALIDATION_PENDING,
    AUTHORIZED,
    DECLINED,
    REVERSED,
    CAPTURED,
    SETTLED,
    FAILED,
    CANCELLED
}
