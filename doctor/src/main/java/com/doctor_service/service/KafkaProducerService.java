package com.doctor_service.service;

import com.doctor_service.event.DoctorCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaProducerService {

    private final KafkaTemplate<String, DoctorCreatedEvent> kafkaTemplate;

    public void publishDoctorCreated(DoctorCreatedEvent event) {

        kafkaTemplate.send("doctor-created", event);

        System.out.println("Doctor event sent to Kafka");

    }
}