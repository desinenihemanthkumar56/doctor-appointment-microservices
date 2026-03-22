package com.booking_service.dto;



import lombok.Data;

@Data
public class PaymentEvent {

    private String appointmentId;
    private String status;

}