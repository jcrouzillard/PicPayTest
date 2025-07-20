package com.julien.payapi.controller;

import com.julien.payapi.dto.PaymentRequest;
import com.julien.payapi.dto.PaymentResponse;
import com.julien.payapi.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
@Validated
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody @Valid PaymentRequest request) {
        var response = paymentService.createPayment(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.findById(id));
    }

    @GetMapping("/token/{token}")
    public ResponseEntity<PaymentResponse> findByToken(@PathVariable String token) {
        return ResponseEntity.ok(paymentService.findByToken(token));
    }
}