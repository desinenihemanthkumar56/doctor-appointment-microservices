package com.doctor_service.event;

import lombok.Data;
import java.util.UUID;

@Data
public class DoctorCreatedEvent {

    private UUID id;
    private UUID userId;
    private String specialization;
    private String city;
}