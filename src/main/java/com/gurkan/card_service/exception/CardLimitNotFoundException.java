package com.gurkan.card_service.exception;

import java.util.UUID;

public class CardLimitNotFoundException extends RuntimeException{
    public CardLimitNotFoundException(UUID cardId) {
        super("Card limit not found for card id: " + cardId);
    }
}
