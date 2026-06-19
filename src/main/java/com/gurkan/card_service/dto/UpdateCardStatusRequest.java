package com.gurkan.card_service.dto;

import com.gurkan.card_service.enums.CardStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCardStatusRequest(@NotNull
                                      CardStatus status) {
}
