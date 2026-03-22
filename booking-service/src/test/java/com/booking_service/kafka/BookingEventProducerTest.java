package com.booking_service.kafka;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BookingEventProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private BookingEventProducer bookingEventProducer;

    @Test
    void shouldPublishBookingEvent() {

        BookingCreatedEvent event = BookingCreatedEvent.builder().build();

        bookingEventProducer.publishBookingCreatedEvent(event);

        verify(kafkaTemplate).send("booking-created", event);
    }
}