package com.doctor_service.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class DoctorSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private UUID doctorId;

    private Long availabilityId;

    private LocalDate appointmentDate;

    private LocalTime slotTime;

    @Enumerated(EnumType.STRING)
    private SlotStatus status;
}