package com.booking_service.client;

import com.booking_service.dto.DoctorSlotResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@FeignClient(name = "doctor-service")
public interface DoctorFeignClient {

    @GetMapping("/api/doctors/{doctorId}/slots")
    List<DoctorSlotResponse> getDoctorSlots(
            @PathVariable UUID doctorId,
            @RequestParam LocalDate date
    );
}