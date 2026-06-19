package com.gurkan.card_service.exception;

import java.util.UUID;

public class InvalidTransactionReversalException extends RuntimeException {
    public InvalidTransactionReversalException(UUID transactionId, String reason) {
        super("Transaction " + transactionId + " cannot be reversed. Reason: " + reason);    }
}
