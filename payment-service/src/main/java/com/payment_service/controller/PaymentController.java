package com.payment_service.controller;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.service.PaymentService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create-intent")
    public PaymentResponse createPayment(@RequestBody PaymentRequest request) throws Exception {

        return paymentService.createPaymentIntent(request);

    }
}