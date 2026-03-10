package com.doctor_service.service;

import com.doctor_service.dto.DoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.entity.Doctor;
import com.doctor_service.event.DoctorCreatedEvent;
import com.doctor_service.exception.ResourceNotFoundException;
import com.doctor_service.repository.DoctorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository doctorRepository;
    private final KafkaProducerService kafkaProducerService;
    public DoctorResponse createDoctor(DoctorRequest request) {

        Doctor doctor = Doctor.builder()
                .userId(request.getUserId())
                .specialization(request.getSpecialization())
                .experienceYears(request.getExperienceYears())
                .consultationFee(request.getConsultationFee())
                .hospitalName(request.getHospitalName())
                .addressLine(request.getAddressLine())
                .area(request.getArea())
                .city(request.getCity())
                .state(request.getState())
                .country(request.getCountry())
                .pincode(request.getPincode())
                .createdAt(LocalDateTime.now())
                .build();

        Doctor saved = doctorRepository.save(doctor);

        // SEND KAFKA EVENT
        DoctorCreatedEvent event = new DoctorCreatedEvent();
        event.setId(saved.getId());
        event.setUserId(saved.getUserId());
        event.setSpecialization(saved.getSpecialization());
        event.setCity(saved.getCity());

        kafkaProducerService.publishDoctorCreated(event);

        return mapToResponse(saved);
    }

    public DoctorResponse getDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found"));

        return mapToResponse(doctor);
    }
    public Page<DoctorResponse> getAllDoctors(int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Doctor> doctors = doctorRepository.findAll(pageable);

        return doctors.map(this::mapToResponse);
    }

    private DoctorResponse mapToResponse(Doctor doctor) {

        return DoctorResponse.builder()
                .id(doctor.getId())
                .userId(doctor.getUserId())
                .specialization(doctor.getSpecialization())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .hospitalName(doctor.getHospitalName())
                .addressLine(doctor.getAddressLine())
                .area(doctor.getArea())
                .city(doctor.getCity())
                .state(doctor.getState())
                .country(doctor.getCountry())
                .pincode(doctor.getPincode())
                .createdAt(doctor.getCreatedAt())
                .build();
    }
}