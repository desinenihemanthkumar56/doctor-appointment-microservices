package com.doctor_service.service;

import com.doctor_service.dto.AvailabilityRequest;
import com.doctor_service.dto.SlotResponse;
import com.doctor_service.entity.DoctorAvailability;
import com.doctor_service.entity.DoctorSlot;
import com.doctor_service.entity.SlotStatus;
import com.doctor_service.repository.AvailabilityRepository;
import com.doctor_service.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final SlotRepository slotRepository;

    public void addAvailability(UUID doctorId, AvailabilityRequest request) {

        DoctorAvailability availability = new DoctorAvailability();

        availability.setDoctorId(doctorId);
        availability.setDate(request.getDate());
        availability.setStartTime(request.getStartTime());
        availability.setEndTime(request.getEndTime());
        availability.setSlotDuration(request.getSlotDuration());

        availabilityRepository.save(availability);

        generateSlots(availability);
    }

    private void generateSlots(DoctorAvailability availability) {

        LocalTime start = availability.getStartTime();
        LocalTime end = availability.getEndTime();

        while (start.isBefore(end)) {

            DoctorSlot slot = new DoctorSlot();

            slot.setDoctorId(availability.getDoctorId());
            slot.setAvailabilityId(availability.getId());
            slot.setAppointmentDate(availability.getDate());
            slot.setSlotTime(start);
            slot.setStatus(SlotStatus.AVAILABLE);

            slotRepository.save(slot);

            start = start.plusMinutes(availability.getSlotDuration());
        }
    }

    public List<SlotResponse> getAvailableSlots(UUID doctorId, LocalDate date) {

        List<DoctorSlot> slots =
                slotRepository.findByDoctorIdAndAppointmentDate(doctorId, date);

        return slots.stream()
                .filter(slot -> slot.getStatus() == SlotStatus.AVAILABLE)
                .map(slot -> new SlotResponse(
                        slot.getId(),
                        slot.getAppointmentDate(),
                        slot.getSlotTime(),
                        slot.getStatus().name()
                ))
                .toList();
    }
}