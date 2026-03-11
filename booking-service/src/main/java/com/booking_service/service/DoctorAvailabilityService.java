package com.booking_service.service;

import com.booking_service.client.DoctorFeignClient;
import com.booking_service.dto.DoctorSlotResponse;
import com.booking_service.exception.ServiceUnavailableException;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorAvailabilityService {

    private final DoctorFeignClient doctorClient;

    private static final String DOCTOR_SERVICE = "doctorService";

    @CircuitBreaker(name = DOCTOR_SERVICE, fallbackMethod = "doctorFallback")
    public List<DoctorSlotResponse> getDoctorSlots(UUID doctorId, LocalDate date) {

        return doctorClient.getDoctorSlots(doctorId, date);
    }

    public List<DoctorSlotResponse> doctorFallback(UUID doctorId, LocalDate date, Throwable ex) {

        throw new ServiceUnavailableException(
                "Doctor service is currently unavailable. Please try again later.");
    }
}