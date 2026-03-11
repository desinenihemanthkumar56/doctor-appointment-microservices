package com.booking_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookingEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    private static final String TOPIC = "booking-created";

    public void publishBookingCreatedEvent(BookingCreatedEvent event) {

        kafkaTemplate.send(TOPIC, event);

        log.info("Booking event published to Kafka: {}", event);
    }
}