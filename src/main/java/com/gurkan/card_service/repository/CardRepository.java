package com.gurkan.card_service.repository;

import com.gurkan.card_service.entity.Card;
import com.gurkan.card_service.enums.CardStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CardRepository extends JpaRepository<Card, UUID> {
    List<Card> findByCustomerId(UUID customerID);

    @Query("""
       SELECT c
       FROM Card c
       WHERE c.customerId = :customerId
       AND c.status = :status
       """)
    List<Card> findActiveCardsByCustomerId(
            @Param("customerId") UUID customerId,
            @Param("status") CardStatus status
    );

    //findCardsByCustomerIdAndStatuses
    @Query("""
        SELECT c
        FROM Card c
        WHERE c.customerId = :customerId
        AND c.status IN ('ACTIVE')
       """)
    List<Card> findCardsByCustomerIdAndStatusActive(
            @Param("customerId") UUID customerId
    );
}
