package com.payment_service.kafka;

import com.payment_service.dto.BookingCreatedEvent;
import com.payment_service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventConsumer {

    private final PaymentService paymentService;

    @KafkaListener(topics = "booking-created", groupId = "payment-service")
    public void consume(BookingCreatedEvent event) {

        log.info("Received booking event: {}", event);

        try {
            paymentService.createPayment(event);
        } catch (Exception e) {
            log.error("Error creating payment for appointment: {}", event.getAppointmentId(), e);
        }
    }
}