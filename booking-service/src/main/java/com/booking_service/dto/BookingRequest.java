package com.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
public class BookingRequest {

    private UUID doctorId;
    private UUID patientId;
    private LocalDate appointmentDate;
    private LocalTime slotTime;
}