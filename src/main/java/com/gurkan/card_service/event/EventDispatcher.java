package com.gurkan.card_service.event;

import com.gurkan.card_service.entity.OutboxEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EventDispatcher {
    private final List<EventConsumer> consumers;
    public void dispatch(OutboxEvent event){
        consumers.stream().filter(
                consumer ->consumer.supports(event.getEventType()))
                .forEach(consumer-> consumer.consume(event));
    }

}
