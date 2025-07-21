package com.julien.payapi.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderMethodName = "hiddenBuilder")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;

    private LocalDateTime createdAt;

    @Column(unique = true, nullable = false, updatable = false)
    private String token;

    public static PaymentBuilder builder() {
        return new CustomPaymentBuilder();
    }

    public void validate() {
        if (PaymentType.PIX.equals(this.paymentType) &&
                this.amount != null &&
                this.amount.compareTo(new BigDecimal("1000")) > 0) {
            throw new IllegalArgumentException("Pagamentos via PIX não podem exceder 1000");
        }
    }

    public void setAmount(BigDecimal amount) {
        if (amount != null) {
            this.amount = amount.setScale(2, RoundingMode.HALF_UP);
        } else {
            this.amount = null;
        }
    }

    private static class CustomPaymentBuilder extends PaymentBuilder {
        @Override
        public Payment build() {
            Payment payment = super.build();
            if (payment.amount != null) {
                payment.amount = payment.amount.setScale(2, RoundingMode.HALF_UP);
            }
            return payment;
        }
    }

}