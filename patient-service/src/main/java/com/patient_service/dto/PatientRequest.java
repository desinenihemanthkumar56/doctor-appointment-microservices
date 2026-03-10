package com.patient_service.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class PatientRequest {

    private String gender;
    private Integer age;
    private LocalDate dateOfBirth;

}