package com.payment_service.repository;

import com.payment_service.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    Optional<Payment> findByAppointmentId(UUID appointmentId);

    Optional<Payment> findByStripeSessionId(String stripeSessionId);

    boolean existsByAppointmentId(UUID appointmentId);
}