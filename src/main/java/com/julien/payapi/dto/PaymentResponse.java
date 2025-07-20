package com.julien.payapi.dto;

import com.julien.payapi.entity.PaymentStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private String description;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
}