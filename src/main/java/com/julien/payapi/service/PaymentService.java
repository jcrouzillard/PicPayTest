package com.julien.payapi.service;

import com.julien.payapi.dto.PaymentRequest;
import com.julien.payapi.dto.PaymentResponse;
import com.julien.payapi.entity.Payment;
import com.julien.payapi.entity.PaymentStatus;
import com.julien.payapi.kafka.PaymentProducer;
import com.julien.payapi.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentProducer paymentProducer;

    public PaymentResponse createPayment(PaymentRequest request) {
        Payment payment = Payment.builder()
                .description(request.getDescription())
                .amount(request.getAmount())
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        Payment saved = paymentRepository.save(payment);
        paymentProducer.send(payment);

        return toResponse(saved);
    }

    public PaymentResponse findById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));

        return toResponse(payment);
    }

    private PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .description(payment.getDescription())
                .amount(payment.getAmount())
                .status(payment.getStatus())
                .createdAt(payment.getCreatedAt())
                .build();
    }
}