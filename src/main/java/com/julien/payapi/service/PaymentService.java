package com.julien.payapi.service;

import com.julien.payapi.dto.PaymentRequest;
import com.julien.payapi.dto.PaymentResponse;
import com.julien.payapi.entity.Payment;
import com.julien.payapi.entity.PaymentStatus;
import com.julien.payapi.kafka.PaymentProducer;
import com.julien.payapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    @Transactional
    public PaymentResponse createPayment(PaymentRequest request) {

        String token = UUID.randomUUID().toString();

        Payment payment = Payment.builder()
                .description(request.getDescription())
                .paymentType(request.getPaymentType())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .token(token)
                .build();

        payment.validate();

        Payment saved = paymentRepository.save(payment);
        paymentProducer.send(payment);

        return toResponse(saved);
    }

    public PaymentResponse findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        return toResponse(payment);
    }

    public PaymentResponse findByToken(String token) {
        Payment payment = paymentRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Payment not found for token: " + token));
        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .description(payment.getDescription())
                .type(payment.getPaymentType())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .token(payment.getToken())
                .build();
    }
}