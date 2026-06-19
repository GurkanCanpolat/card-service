package com.gurkan.card_service.entity;

import com.gurkan.card_service.enums.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Payment {
    @Id
    private UUID id;

    private UUID cardId;

    private BigDecimal amount;

    private String currency;

    private String merchantName;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;



}
