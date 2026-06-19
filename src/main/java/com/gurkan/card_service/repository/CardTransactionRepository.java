package com.gurkan.card_service.repository;

import com.gurkan.card_service.entity.CardTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CardTransactionRepository extends JpaRepository<CardTransaction, UUID> {
    List<CardTransaction> findByCardId(UUID cardId);
    boolean existsByOriginalTransactionId(UUID originalTransactionId);
    Optional<CardTransaction> findByRequestId(UUID requestId);
}
