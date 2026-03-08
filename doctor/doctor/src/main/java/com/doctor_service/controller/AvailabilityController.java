package com.doctor_service.controller;

import com.doctor_service.dto.AvailabilityRequest;
import com.doctor_service.dto.SlotResponse;
import com.doctor_service.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/doctors")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @PostMapping("/{doctorId}/availability")
    public ResponseEntity<?> addAvailability(
            @PathVariable UUID doctorId,
            @RequestBody AvailabilityRequest request) {

        availabilityService.addAvailability(doctorId, request);

        return ResponseEntity.ok("Availability added");
    }

    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<SlotResponse>> getAvailableSlots(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date
    ) {
        return ResponseEntity.ok(
                availabilityService.getAvailableSlots(doctorId, date)
        );
    }
}