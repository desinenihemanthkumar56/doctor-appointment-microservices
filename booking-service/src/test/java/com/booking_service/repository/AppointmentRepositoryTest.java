package com.booking_service.repository;

import com.booking_service.entity.Appointment;
import com.booking_service.entity.PaymentStatus;
import com.booking_service.entity.Status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AppointmentRepositoryTest {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Test
    void shouldFindByPatientId() {

        UUID patientId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();

        Appointment appointment = new Appointment();
        appointment.setDoctorId(doctorId);
        appointment.setPatientId(patientId);
        appointment.setAppointmentDate(LocalDate.now());
        appointment.setSlotTime(LocalTime.of(10,0));
        appointment.setStatus(Status.PENDING);
        appointment.setPaymentStatus(PaymentStatus.PENDING);

        appointmentRepository.save(appointment);

        List<Appointment> result =
                appointmentRepository.findByPatientId(patientId);

        assertFalse(result.isEmpty());
        assertEquals(patientId, result.get(0).getPatientId());
    }
}