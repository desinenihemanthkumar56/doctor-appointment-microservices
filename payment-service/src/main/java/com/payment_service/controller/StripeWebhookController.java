package com.payment_service.controller;

import com.payment_service.service.PaymentService;
import com.stripe.model.Event;
import com.stripe.net.Webhook;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class StripeWebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/webhook")
    public void handleStripeEvent(@RequestBody String payload,
                                  @RequestHeader("Stripe-Signature") String sigHeader) throws Exception {

        Event event = Webhook.constructEvent(payload, sigHeader, endpointSecret);

        paymentService.processStripeEvent(event);
    }
}