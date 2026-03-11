package com.booking_service.controller;

import com.booking_service.dto.BookingRequest;
import com.booking_service.entity.Appointment;
import com.booking_service.service.BookingService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public Appointment createBooking(@RequestBody BookingRequest request) {
        return bookingService.createBooking(request);
    }

    @GetMapping("/{id}")
    public Appointment getBooking(@PathVariable UUID id) {
        return bookingService.getBookingById(id);
    }

    @GetMapping("/patient/{patientId}")
    public List<Appointment> getPatientBookings(@PathVariable UUID patientId) {
        return bookingService.getPatientBookings(patientId);
    }

    @PutMapping("/{id}/cancel")
    public Appointment cancelBooking(@PathVariable UUID id) {
        return bookingService.cancelBooking(id);
    }
}