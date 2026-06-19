package com.gurkan.card_service.exception;

import com.gurkan.card_service.enums.CardStatus;

import java.util.UUID;

public class CardNotActiveException extends RuntimeException{
    public CardNotActiveException(UUID cardId, CardStatus status) {
        super("Card " + cardId + " is not active. Current status: " + status);
    }
}
