package com.payment_service.service;

import com.payment_service.dto.PaymentRequest;
import com.payment_service.dto.PaymentResponse;
import com.payment_service.entity.Payment;
import com.payment_service.repository.PaymentRepository;
import com.payment_service.stripe.StripeClient;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final StripeClient stripeClient;

    public PaymentResponse createPaymentIntent(PaymentRequest request) throws Exception {

        var existing = paymentRepository.findByAppointmentId(request.getAppointmentId());

        if(existing.isPresent()){
            return PaymentResponse.builder()
                    .clientSecret(existing.get().getStripePaymentIntentId())
                    .build();
        }

        var intent = stripeClient.createPaymentIntent(request.getAmount());

        Payment payment = Payment.builder()
                .appointmentId(request.getAppointmentId())
                .amount(request.getAmount())
                .currency("USD")
                .status("CREATED")
                .stripePaymentIntentId(intent.getClientSecret())
                .idempotencyKey(request.getAppointmentId().toString())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return PaymentResponse.builder()
                .clientSecret(intent.getClientSecret())
                .build();
    }
}