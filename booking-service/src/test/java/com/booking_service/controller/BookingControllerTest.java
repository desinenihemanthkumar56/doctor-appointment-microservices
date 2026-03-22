package com.booking_service.controller;

import com.booking_service.entity.Appointment;
import com.booking_service.service.BookingService;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Test
    void shouldGetBookingById() throws Exception {

        UUID bookingId = UUID.randomUUID();

        Appointment appointment = new Appointment();
        appointment.setId(bookingId);

        when(bookingService.getBookingById(bookingId))
                .thenReturn(appointment);

        mockMvc.perform(get("/api/bookings/" + bookingId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}