package com.patient_service.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class PatientResponse {

    private UUID id;
    private UUID userId;

    private String gender;
    private Integer age;
    private LocalDate dateOfBirth;

    private LocalDateTime createdAt;

}