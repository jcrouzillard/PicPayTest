package com.julien.payapi.dto;

import com.julien.payapi.entity.PaymentStatus;
import com.julien.payapi.entity.PaymentType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PaymentResponse {
    private Long id;
    private String description;
    private PaymentType type;
    private BigDecimal amount;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private String token;
}