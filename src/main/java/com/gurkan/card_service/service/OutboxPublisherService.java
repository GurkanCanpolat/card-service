package com.gurkan.card_service.service;

import com.gurkan.card_service.entity.OutboxEvent;
import com.gurkan.card_service.enums.OutboxEventStatus;
import com.gurkan.card_service.event.EventDispatcher;
import com.gurkan.card_service.kafka.KafkaTopics;
import com.gurkan.card_service.event.TransactionEventProducer;
import com.gurkan.card_service.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class OutboxPublisherService {
    private final OutboxEventRepository outboxEventRepository;
    private final EventDispatcher eventDispatcher;
    private final TransactionEventProducer producer;

    @Scheduled(fixedDelay = 5000) // Her 5 saniyede bir çalışır
    @Transactional
    public void publishPendingEvents(){
        List<OutboxEvent> pendingEvents = outboxEventRepository.findTop10ByStatusOrderByCreatedAtAsc(com.gurkan.card_service.enums.OutboxEventStatus.PENDING);

        for(OutboxEvent event : pendingEvents){
            try{
                publish(event);
                event.setStatus(OutboxEventStatus.PUBLISHED);
                event.setPublishedAt(Instant.now());
                event.setErrorMessage(null);
                outboxEventRepository.save(event);
            } catch (Exception exception)
            {
                event.setStatus(OutboxEventStatus.FAILED);
                event.setErrorMessage(exception.getMessage());
                outboxEventRepository.save(event);
            }
        }
    }

    private void publish(OutboxEvent event) {
        log.info(
                "Publishing outbox event. id={}, eventType={}",
                event.getId(),
                event.getEventType()
        );

        eventDispatcher.dispatch(event);

        if ("TRANSACTION_CREATED".equals(event.getEventType())) {
            eventDispatcher.dispatch(event);
        }
        producer.publish(
                KafkaTopics.TRANSACTION_EVENTS,
                event.getAggregateId().toString(),
                event.getPayload()
        );
    }
}
