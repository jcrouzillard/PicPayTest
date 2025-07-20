package com.julien.payapi.service;

import com.julien.payapi.dto.PaymentRequest;
import com.julien.payapi.entity.Payment;
import com.julien.payapi.entity.PaymentStatus;
import com.julien.payapi.kafka.PaymentProducer;
import com.julien.payapi.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentRepository paymentRepository;
    private PaymentProducer paymentProducer;
    private PaymentService paymentService;

    @BeforeEach
    void setup() {
        paymentRepository = mock(PaymentRepository.class);
        paymentProducer = mock(PaymentProducer.class);
        paymentService = new PaymentService(paymentRepository, paymentProducer);
    }

    @Test
    void testCreatePayment() {
        PaymentRequest request = new PaymentRequest();
        request.setDescription("Test Payment");
        request.setAmount(BigDecimal.valueOf(150.00));

        Payment saved = Payment.builder()
                .id(1L)
                .description("Test Payment")
                .amount(BigDecimal.valueOf(150.00))
                .status(PaymentStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .build();

        when(paymentRepository.save(any(Payment.class))).thenReturn(saved);

        var response = paymentService.createPayment(request);

        assertNotNull(response);
        assertEquals(saved.getId(), response.getId());
        assertEquals(PaymentStatus.PENDING, response.getStatus());

        verify(paymentProducer).send(any(Payment.class));
    }

    @Test
    void testFindById_existingPayment() {
        Payment payment = Payment.builder()
                .id(10L)
                .description("Saved Payment")
                .amount(BigDecimal.TEN)
                .status(PaymentStatus.PROCESSED)
                .createdAt(LocalDateTime.now())
                .build();

        when(paymentRepository.findById(10L)).thenReturn(Optional.of(payment));

        var response = paymentService.findById(10L);

        assertEquals(10L, response.getId());
        assertEquals(PaymentStatus.PROCESSED, response.getStatus());
    }

    @Test
    void testFindById_notFound_shouldThrow() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> paymentService.findById(999L));

        assertTrue(ex.getMessage().contains("Payment not found"));
    }
}