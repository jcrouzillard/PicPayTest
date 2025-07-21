package com.julien.payapi.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name("payment-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic retryTopic() {
        return TopicBuilder.name("payment-retry-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic dlqTopic() {
        return TopicBuilder.name("payment-dlq-topic")
                .partitions(1)
                .replicas(1)
                .build();
    }
}
