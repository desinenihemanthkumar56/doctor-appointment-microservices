package com.payment_service.kafka;

import com.payment_service.dto.PaymentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentEventProducer {

    private final KafkaTemplate<String, PaymentEvent> kafkaTemplate;

    private static final String TOPIC_SUCCESS = "payment-success";
    private static final String TOPIC_FAILED = "payment-failed";

    public void sendPaymentSuccess(String appointmentId, Long amount){

        PaymentEvent event = new PaymentEvent(
                appointmentId,
                amount,
                "SUCCESS"
        );

        kafkaTemplate.send(TOPIC_SUCCESS, event);
    }

    public void sendPaymentFailed(String appointmentId, Long amount){

        PaymentEvent event = new PaymentEvent(
                appointmentId,
                amount,
                "FAILED"
        );

        kafkaTemplate.send(TOPIC_FAILED, event);
    }
}