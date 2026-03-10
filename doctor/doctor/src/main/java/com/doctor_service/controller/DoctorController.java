package com.doctor_service.controller;

import com.doctor_service.dto.DoctorRequest;
import com.doctor_service.dto.DoctorResponse;
import com.doctor_service.dto.DoctorSearchRequest;
import com.doctor_service.dto.DoctorSearchResponse;
import com.doctor_service.service.DoctorSearchService;
import com.doctor_service.service.DoctorService;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    private final DoctorSearchService doctorSearchService;

    @PostMapping
    public ResponseEntity<DoctorResponse> createDoctor(
            @RequestBody DoctorRequest request) {

        return ResponseEntity.ok(
                doctorService.createDoctor(request)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorResponse> getDoctor(
            @PathVariable UUID id) {

        return ResponseEntity.ok(
                doctorService.getDoctor(id)
        );
    }

    @GetMapping
    public ResponseEntity<Page<DoctorResponse>> getDoctors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(doctorService.getAllDoctors(page, size));
    }


    @GetMapping("/search")
    public List<DoctorSearchResponse> searchDoctors(

            @RequestParam(required = false) String city,
            @RequestParam(required = false) String area,
            @RequestParam(required = false) String state,
            @RequestParam(required = false) String specialization
    ) {

        DoctorSearchRequest request = new DoctorSearchRequest();

        request.setCity(city);
        request.setArea(area);
        request.setState(state);
        request.setSpecialization(specialization);

        return doctorSearchService.searchDoctors(request);
    }
}