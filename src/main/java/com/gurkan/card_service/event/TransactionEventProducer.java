package com.gurkan.card_service.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void publish(
            String topic,
            String key,
            String payload
    ) {

        log.info(
                "Publishing event. topic={}, key={}",
                topic,
                key
        );

        kafkaTemplate.send(
                topic,
                key,
                payload
        );
    }
}
