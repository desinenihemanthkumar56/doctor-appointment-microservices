package com.booking_service.service;

import com.booking_service.client.PatientFeignClient;
import com.booking_service.dto.BookingRequest;
import com.booking_service.dto.DoctorSlotResponse;
import com.booking_service.entity.Appointment;
import com.booking_service.entity.PaymentStatus;
import com.booking_service.entity.Status;
import com.booking_service.exception.BookingNotFoundException;
import com.booking_service.exception.SlotAlreadyBookedException;
import com.booking_service.exception.SlotNotFoundException;
import com.booking_service.kafka.BookingCreatedEvent;
import com.booking_service.kafka.BookingEventProducer;
import com.booking_service.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailabilityService doctorAvailabilityService;
    private final PatientFeignClient patientClient;
    private final BookingEventProducer bookingEventProducer;

    public Appointment createBooking(BookingRequest request) {

        // 1️⃣ Validate patient
        patientClient.getPatient(request.getPatientId());

        // 2️⃣ Get doctor slots
        List<DoctorSlotResponse> slots =
                doctorAvailabilityService.getDoctorSlots(
                        request.getDoctorId(),
                        request.getAppointmentDate()
                );

        // 3️⃣ Validate slot exists
        DoctorSlotResponse selectedSlot =
                slots.stream()
                        .filter(slot -> slot.getSlotTime().equals(request.getSlotTime()))
                        .findFirst()
                        .orElseThrow(() ->
                                new SlotNotFoundException("Requested slot does not exist"));

        // 4️⃣ Check availability
        if (!selectedSlot.getStatus().equalsIgnoreCase("AVAILABLE")) {
            throw new SlotAlreadyBookedException("Slot already booked");
        }

        // 5️⃣ Create appointment
        Appointment appointment = Appointment.builder()
                .doctorId(request.getDoctorId())
                .patientId(request.getPatientId())
                .appointmentDate(request.getAppointmentDate())
                .slotTime(request.getSlotTime())
                .status(Status.PENDING)
                .paymentStatus(PaymentStatus.PENDING)
                .build();

        try {

            Appointment savedAppointment = appointmentRepository.save(appointment);

            // Create Kafka Event
            BookingCreatedEvent event = BookingCreatedEvent.builder()
                    .appointmentId(savedAppointment.getId())
                    .doctorId(savedAppointment.getDoctorId())
                    .patientId(savedAppointment.getPatientId())
                    .appointmentDate(savedAppointment.getAppointmentDate())
                    .slotTime(savedAppointment.getSlotTime())
                    .status(savedAppointment.getStatus().name())
                    .build();

            // Publish Kafka Event
            bookingEventProducer.publishBookingCreatedEvent(event);

            return savedAppointment;

        } catch (DataIntegrityViolationException ex) {

            throw new SlotAlreadyBookedException(
                    "Another patient already booked this slot. Please choose another slot.");
        }
    }
    public Appointment getBookingById(UUID id) {

        return appointmentRepository.findById(id)
                .orElseThrow(() ->
                        new BookingNotFoundException("Booking not found for id: " + id));
    }

    public List<Appointment> getPatientBookings(UUID patientId) {

        return appointmentRepository.findByPatientId(patientId);
    }

    public Appointment cancelBooking(UUID id) {

        Appointment appointment = getBookingById(id);

        appointment.setStatus(Status.CANCELLED);

        return appointmentRepository.save(appointment);
    }
}