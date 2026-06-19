package com.gurkan.card_service.config;

import com.gurkan.card_service.entity.*;
import com.gurkan.card_service.enums.CardStatus;
import com.gurkan.card_service.enums.CardType;
import com.gurkan.card_service.enums.TransactionStatus;
import com.gurkan.card_service.enums.TransactionType;
import com.gurkan.card_service.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

/**
 * Sample data initializer that runs on application startup
 * This class populates the database with sample data for testing and development
 */
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("!prod")  // Only run in non-production environments
public class DataInitializer implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final CardRepository cardRepository;
    private final CardLimitRepository cardLimitRepository;
    private final CardTransactionRepository cardTransactionRepository;

    @Override
    public void run(String... args) {
        log.info("Starting data initialization...");

        // Check if data already exists
        if (customerRepository.count() > 0) {
            log.info("Database already contains data. Skipping initialization.");
            return;
        }

        // Create sample customers
        Customer customer1 = createCustomer(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440001"),
                "Ahmet Yilmaz",
                "ahmet.yilmaz@example.com",
                "+90-532-1234567"
        );

        Customer customer2 = createCustomer(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440002"),
                "Fatma Demir",
                "fatma.demir@example.com",
                "+90-533-2345678"
        );

        Customer customer3 = createCustomer(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440003"),
                "Mehmet Kaya",
                "mehmet.kaya@example.com",
                "+90-534-3456789"
        );

        createCustomer(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440004"),
                "Zeynep Ozturk",
                "zeynep.ozturk@example.com",
                "+90-535-4567890"
        );

        log.info("Created 4 sample customers");

        // Create sample cards
        Card card1 = createCard(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440011"),
                customer1.getId(),
                "5424180123456789",
                CardType.CREDIT,
                CardStatus.ACTIVE
        );

        Card card2 = createCard(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440012"),
                customer1.getId(),
                "4532123456789012",
                CardType.DEBIT,
                CardStatus.ACTIVE
        );

        Card card3 = createCard(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440013"),
                customer2.getId(),
                "5412345678901234",
                CardType.CREDIT,
                CardStatus.ACTIVE
        );

        Card card4 = createCard(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440014"),
                customer3.getId(),
                "4532111111111111",
                CardType.DEBIT,
                CardStatus.BLOCKED
        );

        log.info("Created 4 sample cards");

        // Create sample card limits
        createCardLimit(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440021"),
                card1.getId(),
                BigDecimal.valueOf(50000.00),
                BigDecimal.valueOf(45000.00)
        );

        createCardLimit(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440022"),
                card2.getId(),
                BigDecimal.valueOf(100000.00),
                BigDecimal.valueOf(100000.00)
        );

        createCardLimit(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440023"),
                card3.getId(),
                BigDecimal.valueOf(75000.00),
                BigDecimal.valueOf(60000.00)
        );

        createCardLimit(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440024"),
                card4.getId(),
                BigDecimal.valueOf(30000.00),
                BigDecimal.valueOf(30000.00)
        );

        log.info("Created 4 sample card limits");

        // Create sample transactions
        CardTransaction trans1 = createCardTransaction(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440031"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440041"),
                card1.getId(),
                BigDecimal.valueOf(1500.00),
                "TRY",
                TransactionType.PURCHASE,
                TransactionStatus.APPROVED
        );

        createCardTransaction(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440032"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440042"),
                card1.getId(),
                BigDecimal.valueOf(2500.00),
                "TRY",
                TransactionType.PURCHASE,
                TransactionStatus.SETTLED
        );

        createCardTransaction(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440033"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440043"),
                card3.getId(),
                BigDecimal.valueOf(5000.00),
                "TRY",
                TransactionType.PURCHASE,
                TransactionStatus.PENDING_SETTLEMENT
        );

        createCardTransaction(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440034"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440044"),
                card2.getId(),
                BigDecimal.valueOf(750.00),
                "TRY",
                TransactionType.CASH_WITHDRAWAL,
                TransactionStatus.SETTLED
        );

        CardTransaction trans5 = createCardTransaction(
                UUID.fromString("550e8400-e29b-41d4-a716-446655440035"),
                UUID.fromString("550e8400-e29b-41d4-a716-446655440045"),
                card1.getId(),
                BigDecimal.valueOf(1500.00),
                "TRY",
                TransactionType.REFUND,
                TransactionStatus.SETTLED
        );

        trans5.setOriginalTransactionId(trans1.getId());
        cardTransactionRepository.save(trans5);

        log.info("Created 5 sample transactions");
        log.info("Data initialization completed successfully!");
    }

    private Customer createCustomer(UUID id, String fullName, String email, String phoneNumber) {
        Instant now = Instant.now();
        Customer customer = Customer.builder()
                .id(id)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return customerRepository.save(customer);
    }

    private Card createCard(UUID id, UUID customerId, String cardNumber, CardType cardType, CardStatus status) {
        Instant now = Instant.now();
        Card card = Card.builder()
                .id(id)
                .customerId(customerId)
                .cardNumber(cardNumber)
                .cardType(cardType)
                .status(status)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return cardRepository.save(card);
    }

    private CardLimit createCardLimit(UUID id, UUID cardId, BigDecimal totalLimit, BigDecimal availableLimit) {
        Instant now = Instant.now();
        CardLimit cardLimit = CardLimit.builder()
                .id(id)
                .cardId(cardId)
                .totalLimit(totalLimit)
                .availableLimit(availableLimit)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return cardLimitRepository.save(cardLimit);
    }

    private CardTransaction createCardTransaction(
            UUID id, UUID requestId, UUID cardId, BigDecimal amount,
            String currency, TransactionType type, TransactionStatus status) {
        Instant now = Instant.now();
        CardTransaction transaction = CardTransaction.builder()
                .id(id)
                .requestId(requestId)
                .cardId(cardId)
                .amount(amount)
                .currency(currency)
                .type(type)
                .status(status)
                .createdAt(now)
                .updatedAt(now)
                .build();
        return cardTransactionRepository.save(transaction);
    }
}

