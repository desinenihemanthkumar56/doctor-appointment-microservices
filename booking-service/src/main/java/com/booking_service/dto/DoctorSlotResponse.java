package com.booking_service.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class DoctorSlotResponse {

    private Long slotId;
    private LocalDate appointmentDate;
    private LocalTime slotTime;
    private String status;
}