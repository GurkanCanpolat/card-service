package com.gurkan.card_service.config;

import com.gurkan.card_service.kafka.KafkaTopics;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic transactionEventsTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_EVENTS)
                .partitions(3)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic transactionEventsDltTopic() {
        return TopicBuilder.name(KafkaTopics.TRANSACTION_EVENTS_DLT)
                .partitions(3)
                .replicas(1)
                .build();
    }
}
