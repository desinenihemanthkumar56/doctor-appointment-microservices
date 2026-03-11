package com.booking_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "patient-service")
public interface PatientFeignClient {

    @GetMapping("/api/patients/{id}")
    Object getPatient(@PathVariable UUID id);

}