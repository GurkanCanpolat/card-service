package com.gurkan.card_service.enums;

public enum TransactionStatus {
    RECEIVED,

    VALIDATING,

    APPROVED,

    DECLINED,

    PENDING_SETTLEMENT,

    SETTLED,

    REVERSED,

    FAILED,

    TIMEOUT
}
