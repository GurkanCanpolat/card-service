package com.gurkan.card_service.service;

import com.gurkan.card_service.entity.OutboxEvent;
import com.gurkan.card_service.event.EventConsumer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AuditConsumerService implements EventConsumer {
    @Override
    public boolean supports(String eventType) {
        return true;
    }

    @Override
    public void consume(OutboxEvent event) {
        log.info(
                "Audit event created. aggregateId={}",
                event.getAggregateId()
        );
    }
}
