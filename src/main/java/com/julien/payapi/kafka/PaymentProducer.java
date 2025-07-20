package com.julien.payapi.kafka;

import com.julien.payapi.entity.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentProducer {

    private final KafkaTemplate<String, Payment> kafkaTemplate;

    private static final String TOPIC = "payments";

    public void send(Payment payment) {
        log.info("Producing payment to Kafka: {}", payment);
        kafkaTemplate.send(TOPIC, payment);
    }
}