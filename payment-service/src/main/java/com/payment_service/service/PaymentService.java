package com.payment_service.service;

import com.payment_service.dto.BookingCreatedEvent;
import com.payment_service.dto.PaymentRequest;
import com.payment_service.entity.Payment;
import com.payment_service.entity.PaymentStatus;
import com.payment_service.feign.DoctorClient;
import com.payment_service.kafka.PaymentEventProducer;
import com.payment_service.repository.PaymentRepository;
import com.payment_service.stripe.StripeClient;
import com.stripe.model.Event;

import com.stripe.model.checkout.Session;



import java.time.LocalDateTime;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.stripe.exception.EventDataObjectDeserializationException;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

    private final DoctorClient doctorClient;
    private final PaymentRepository paymentRepository;
    private final PaymentEventProducer paymentEventProducer;
    private final StripeClient stripeClient;




    /*
     CREATE CHECKOUT SESSION
     */
    public String createCheckoutSession(PaymentRequest request) throws Exception {

        var existing = paymentRepository.findByAppointmentId(request.getAppointmentId());

        if (existing.isPresent()) {
            return existing.get().getStripeSessionId();
        }

        Session session = stripeClient.createCheckoutSession(
                request.getAmount(),
                request.getAppointmentId().toString()
        );

        Payment payment = Payment.builder()
                .appointmentId(request.getAppointmentId())
                .amount(request.getAmount())
                .currency("USD")
                .status(PaymentStatus.CREATED)
                .stripeSessionId(session.getId())
                .idempotencyKey(request.getAppointmentId().toString())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        return session.getUrl();
    }


    @Transactional
    public void processStripeEvent(Event event) throws EventDataObjectDeserializationException {

        log.info("Stripe Event Received: {}", event.getType());

        Session session = (Session) event
                .getDataObjectDeserializer()
                .deserializeUnsafe();

        if (session == null) {
            log.error("Stripe session is null");
            return;
        }

        String sessionId = session.getId();

        log.info("Stripe Session ID: {}", sessionId);

        paymentRepository.findByStripeSessionId(sessionId)
                .ifPresent(payment -> {

                /*
                 SUCCESS CASE
                 */
                    if ("checkout.session.completed".equals(event.getType())) {

                        payment.setStatus(PaymentStatus.SUCCESS);
                        payment.setUpdatedAt(LocalDateTime.now());

                        paymentRepository.save(payment);

                        log.info("Payment updated SUCCESS for appointment {}",
                                payment.getAppointmentId());

                        paymentEventProducer.sendPaymentSuccess(
                                payment.getAppointmentId().toString(),
                                payment.getAmount()
                        );
                    }

                /*
                 FAILED / EXPIRED CASE
                 */
                    if ("checkout.session.expired".equals(event.getType())
                            || "payment_intent.payment_failed".equals(event.getType())) {

                        payment.setStatus(PaymentStatus.FAILED);
                        payment.setUpdatedAt(LocalDateTime.now());

                        paymentRepository.save(payment);

                        log.info("Payment updated FAILED for appointment {}",
                                payment.getAppointmentId());

                        paymentEventProducer.sendPaymentFailed(
                                payment.getAppointmentId().toString(),
                                payment.getAmount()
                        );
                    }

                });
    }

    public void createPayment(BookingCreatedEvent event) throws Exception {

        // Prevent duplicate payment creation
        if (paymentRepository.existsByAppointmentId(event.getAppointmentId())) {
            return;
        }

        // Get doctor fee from Doctor Service
        var doctor = doctorClient.getDoctor(event.getDoctorId());
        Long amount = doctor.getConsultationFee();

        // Create Stripe Checkout Session
        var session = stripeClient.createCheckoutSession(
                amount,
                event.getAppointmentId().toString()
        );

        // Save payment record
        Payment payment = Payment.builder()
                .appointmentId(event.getAppointmentId())
                .amount(amount)
                .currency("USD")
                .status(PaymentStatus.PROCESSING)
                .stripeSessionId(session.getId()) // store sessionId
                .idempotencyKey(event.getAppointmentId().toString())
                .createdAt(LocalDateTime.now())
                .build();

        paymentRepository.save(payment);

        System.out.println("Stripe Checkout URL: " + session.getUrl());
    }
}