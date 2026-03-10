package com.patient_service.kafka;

import com.patient_service.event.DoctorCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class DoctorEventConsumer {

    @KafkaListener(topics = "doctor-created", groupId = "patient-service")
    public void listen(DoctorCreatedEvent event) {

        log.info("Doctor event received: {}", event);

    }
}