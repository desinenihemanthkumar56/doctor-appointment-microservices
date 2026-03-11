package com.booking_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
public class BookingResponse {

    private UUID appointmentId;

    private UUID doctorId;

    private UUID patientId;

    private LocalDate appointmentDate;

    private LocalTime slotTime;

    private String status;

}