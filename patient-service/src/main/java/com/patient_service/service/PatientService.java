package com.patient_service.service;

import com.patient_service.dto.PatientRequest;
import com.patient_service.dto.PatientResponse;
import com.patient_service.entity.Patient;
import com.patient_service.exception.PatientNotFoundException;
import com.patient_service.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;

    public PatientResponse createPatient(UUID userId, PatientRequest request) {

        Patient patient = Patient.builder()
                .userId(userId)
                .gender(request.getGender())
                .age(request.getAge())
                .dateOfBirth(request.getDateOfBirth())
                .createdAt(LocalDateTime.now())
                .build();

        Patient savedPatient = patientRepository.save(patient);

        return mapToResponse(savedPatient);
    }

    public PatientResponse getPatientProfile(UUID userId) {

        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        return mapToResponse(patient);
    }

    public PatientResponse getPatientById(UUID id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new PatientNotFoundException("Patient not found"));

        return mapToResponse(patient);
    }

    private PatientResponse mapToResponse(Patient patient) {

        return PatientResponse.builder()
                .id(patient.getId())
                .userId(patient.getUserId())
                .gender(patient.getGender())
                .age(patient.getAge())
                .dateOfBirth(patient.getDateOfBirth())
                .createdAt(patient.getCreatedAt())
                .build();
    }

}