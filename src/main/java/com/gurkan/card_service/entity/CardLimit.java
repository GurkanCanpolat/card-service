package com.gurkan.card_service.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "card_limits")
public class CardLimit {
    @Id
    private UUID id;

    private UUID cardId;

    private BigDecimal totalLimit;

    private BigDecimal availableLimit;

    private Instant createdAt;

    private Instant updatedAt;
}
