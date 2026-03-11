package com.booking_service.kafka;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingCreatedEvent {

    private UUID appointmentId;

    private UUID doctorId;

    private UUID patientId;

    private LocalDate appointmentDate;

    private LocalTime slotTime;

    private String status;

}