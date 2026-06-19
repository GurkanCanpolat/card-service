package com.gurkan.card_service.entity;

import com.gurkan.card_service.enums.CardStatus;
import com.gurkan.card_service.enums.CardType;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "cards")
public class Card {
    @Id
    private UUID id;

    private UUID customerId;

    private String cardNumber;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    private Instant createdAt;

    private Instant updatedAt;

}
