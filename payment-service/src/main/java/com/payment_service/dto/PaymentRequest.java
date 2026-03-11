package com.payment_service.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class PaymentRequest {

    private UUID appointmentId;

    private Long amount;
}