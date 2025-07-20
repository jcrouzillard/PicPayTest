package com.julien.payapi.kafka;

import com.julien.payapi.entity.Payment;
import com.julien.payapi.entity.PaymentStatus;
import com.julien.payapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentConsumer {

    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "payments", groupId = "payapi-consumer")
    public void consume(Payment incomingPayment) {
        log.info("Consumed payment from Kafka: {}", incomingPayment);

        paymentRepository.findById(incomingPayment.getId()).ifPresentOrElse(payment -> {
            payment.setStatus(PaymentStatus.PROCESSED);
            paymentRepository.save(payment);
            log.info("Payment marked as PROCESSED: {}", payment.getId());
        }, () -> {
            log.warn("Payment not found in database: {}", incomingPayment.getId());
        });
    }
}