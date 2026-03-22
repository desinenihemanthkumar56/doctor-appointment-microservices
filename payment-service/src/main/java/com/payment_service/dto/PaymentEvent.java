package com.payment_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentEvent {

    private String appointmentId;
    private Long amount;
    private String status;


}