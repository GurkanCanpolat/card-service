package com.gurkan.card_service.kafka;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TransactionEventDltConsumer {
    @KafkaListener(
            topics = KafkaTopics.TRANSACTION_EVENTS_DLT,
            groupId = "notification-dlt-group"
    )
    public void consumeDlt(String payload) {
        log.error(
                "DLT message received. payload={}",
                payload
        );
    }
}
