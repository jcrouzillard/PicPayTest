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
public class RetryPaymentConsumer {

    private final PaymentRepository paymentRepository;

    @KafkaListener(topics = "payment-retry-topic", groupId = "payment-retry-group")
    public void consumeRetry(Payment payment) {
        log.info("[RETRY] Consumindo pagamento: {}", payment);
        payment.setStatus(PaymentStatus.PROCESSED);
        paymentRepository.save(payment);

        // nova tentativa de processamento
        if ("erro".equalsIgnoreCase(payment.getDescription())) {
            log.error("[RETRY] Falha novamente. Enviando para DLQ");
            throw new RuntimeException("Erro no Retry");
        }

        // processamento ok
    }
}