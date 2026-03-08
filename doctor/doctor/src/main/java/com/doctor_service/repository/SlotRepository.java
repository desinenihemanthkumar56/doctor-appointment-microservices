package com.doctor_service.repository;

import com.doctor_service.entity.DoctorSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SlotRepository extends JpaRepository<DoctorSlot, Long> {

    List<DoctorSlot> findByDoctorIdAndAppointmentDate(UUID doctorId, LocalDate appointmentDate);

    Optional<DoctorSlot> findByDoctorIdAndAppointmentDateAndSlotTime(
            UUID doctorId,
            LocalDate appointmentDate,
            java.time.LocalTime slotTime
    );

}