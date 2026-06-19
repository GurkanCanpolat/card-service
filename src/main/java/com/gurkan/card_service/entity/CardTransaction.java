package com.gurkan.card_service.entity;

import com.gurkan.card_service.enums.TransactionStatus;
import com.gurkan.card_service.enums.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "card_transactions")
public class CardTransaction {
    @Id
    private UUID id;

    private UUID requestId;

    private UUID cardId;

    private BigDecimal amount;

    private String currency;

    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    private Instant createdAt;

    private UUID originalTransactionId;

    private Instant updatedAt;
}
