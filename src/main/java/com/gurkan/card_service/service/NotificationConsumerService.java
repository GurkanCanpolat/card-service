package com.gurkan.card_service.service;

import com.gurkan.card_service.entity.OutboxEvent;
import com.gurkan.card_service.event.EventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationConsumerService implements EventConsumer {
    @Override
    public boolean supports(String eventType) {
        return "TRANSACTION_CREATED".equals(eventType);
    }

    @Override
    public void consume(OutboxEvent event) {
        if ("TRANSACTION_CREATED".equals(event.getEventType())) {
            log.info(
                    "Notification sent for transaction {}",
                    event.getAggregateId()
            );
        }
    }
}
