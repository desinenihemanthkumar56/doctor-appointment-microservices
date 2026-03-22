package com.payment_service.feign;

import com.payment_service.dto.DoctorResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@FeignClient(name = "doctor-service")
public interface DoctorClient {

    @GetMapping("/api/doctors/{doctorId}")
    DoctorResponse getDoctor(@PathVariable UUID doctorId);
}