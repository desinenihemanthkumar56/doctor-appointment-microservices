package com.patient_service.controller;

import com.patient_service.dto.PatientRequest;
import com.patient_service.dto.PatientResponse;
import com.patient_service.service.PatientService;

import io.swagger.v3.oas.annotations.Parameter;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class PatientController {

    private final PatientService patientService;

    @PostMapping
    public PatientResponse createPatient(

            @Parameter(description = "User ID from Auth Service")
            @RequestHeader("X-USER-ID") UUID userId,

            @RequestBody PatientRequest request
    ) {

        return patientService.createPatient(userId, request);
    }

    @GetMapping("/me")
    public PatientResponse getMyProfile(

            @Parameter(description = "User ID from Auth Service")
            @RequestHeader("X-USER-ID") UUID userId
    ) {

        return patientService.getPatientProfile(userId);
    }

    @GetMapping("/{id}")
    public PatientResponse getPatientById(
            @PathVariable UUID id
    ) {

        return patientService.getPatientById(id);
    }
}