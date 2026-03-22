package com.booking_service.kafka;

import com.booking_service.dto.PaymentEvent;
import com.booking_service.entity.Appointment;
import com.booking_service.entity.PaymentStatus;
import com.booking_service.entity.Status;
import com.booking_service.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentEventConsumer {

    private final AppointmentRepository appointmentRepository;

    @KafkaListener(topics = "payment-success", groupId = "booking-group")
    public void handlePaymentSuccess(PaymentEvent event){

        System.out.println("Payment success received for appointment: " + event.getAppointmentId());

        appointmentRepository.findById(UUID.fromString(event.getAppointmentId()))
                .ifPresent(appointment -> {

                    // Update payment status
                    appointment.setPaymentStatus(PaymentStatus.SUCCESS);

                    // Update appointment status
                    appointment.setStatus(Status.CONFIRMED);

                    appointmentRepository.save(appointment);

                    System.out.println("Appointment confirmed and payment marked SUCCESS");
                });
    }
    @KafkaListener(topics = "payment-failed", groupId = "booking-group")
    public void handlePaymentFailed(PaymentEvent event){

        appointmentRepository.findById(UUID.fromString(event.getAppointmentId()))
                .ifPresent(appointment -> {

                    appointment.setPaymentStatus(PaymentStatus.FAILED);

                    appointment.setStatus(Status.FAILED);

                    appointmentRepository.save(appointment);

                    System.out.println("Payment failed for appointment");
                });
    }
}