package com.gurkan.card_service.event;

import com.gurkan.card_service.entity.OutboxEvent;

public interface EventConsumer {
    boolean supports(String eventType);

    void consume(OutboxEvent event);
}
