package com.booking_service.service;

import com.booking_service.client.PatientFeignClient;
import com.booking_service.dto.BookingRequest;
import com.booking_service.dto.DoctorSlotResponse;
import com.booking_service.entity.Appointment;
import com.booking_service.entity.PaymentStatus;
import com.booking_service.entity.Status;
import com.booking_service.kafka.BookingEventProducer;


import com.booking_service.repository.AppointmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private DoctorAvailabilityService doctorAvailabilityService;

    @Mock
    private PatientFeignClient patientFeignClient;

    @Mock
    private BookingEventProducer bookingEventProducer;

    @InjectMocks
    private BookingService bookingService;

    @Test
    void shouldCreateBookingSuccessfully() {

        UUID doctorId = UUID.randomUUID();
        UUID patientId = UUID.randomUUID();

        BookingRequest request = BookingRequest.builder()
                .doctorId(doctorId)
                .patientId(patientId)
                .appointmentDate(LocalDate.now())
                .slotTime(LocalTime.of(10, 0))
                .build();

        DoctorSlotResponse slot = new DoctorSlotResponse();
        slot.setSlotTime(LocalTime.of(10,0));
        slot.setStatus("AVAILABLE");

        when(doctorAvailabilityService.getDoctorSlots(any(), any()))
                .thenReturn(List.of(slot));

        Appointment savedAppointment = Appointment.builder()
                .id(UUID.randomUUID())
                .doctorId(doctorId)
                .patientId(patientId)
                .appointmentDate(LocalDate.now())
                .slotTime(LocalTime.of(10,0))
                .status(Status.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        when(appointmentRepository.save(any())).thenReturn(savedAppointment);

        Appointment result = bookingService.createBooking(request);

        assertNotNull(result);
        assertEquals(Status.PENDING, result.getStatus());

        verify(appointmentRepository).save(any());
        verify(bookingEventProducer).publishBookingCreatedEvent(any());
    }
}