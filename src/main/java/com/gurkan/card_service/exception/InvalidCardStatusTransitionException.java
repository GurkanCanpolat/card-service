package com.gurkan.card_service.exception;

import com.gurkan.card_service.enums.CardStatus;

public class InvalidCardStatusTransitionException extends RuntimeException{
    public InvalidCardStatusTransitionException(
            CardStatus currentStatus,
            CardStatus targetStatus
    ) {
        super(
                "Card status cannot be changed from "
                        + currentStatus
                        + " to "
                        + targetStatus
        );
    }
}
