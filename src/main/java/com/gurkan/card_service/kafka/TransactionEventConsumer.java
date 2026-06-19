package com.gurkan.card_service.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gurkan.card_service.dto.TransactionCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class TransactionEventConsumer {
    private ObjectMapper objectMapper;

    @KafkaListener(
            topics = KafkaTopics.TRANSACTION_EVENTS,
            groupId = "notification-group"
    )
    public void consume(String payload) throws Exception{

            TransactionCreatedEvent event =
                    objectMapper.readValue(
                            payload,
                            TransactionCreatedEvent.class
                    );

            log.info(
                    "Transaction consumed. transactionId={}, amount={}",
                    event.transactionId(),
                    event.amount()
            );
        if (event.amount().intValue() == 9999) {
            throw new RuntimeException("Simulated consumer failure");
        }

    }
}
