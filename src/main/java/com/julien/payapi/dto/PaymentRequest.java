package com.julien.payapi.dto;

import com.julien.payapi.entity.PaymentStatus;
import com.julien.payapi.entity.PaymentType;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequest {
    private String description;

    @NotNull(message = "payment type is required")
    private PaymentType paymentType;

    @NotNull
    @DecimalMin("0.01")
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
}