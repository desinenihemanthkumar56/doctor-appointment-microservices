package com.payment_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class BookingCreatedEvent {

    private UUID appointmentId;
    private UUID doctorId;
    private UUID patientId;
    private LocalDate appointmentDate;
    private LocalTime slotTime;
    private String status;
}