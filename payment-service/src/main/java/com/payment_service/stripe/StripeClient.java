package com.payment_service.stripe;

import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import org.springframework.stereotype.Component;

@Component
public class StripeClient {

    public PaymentIntent createPaymentIntent(Long amount) throws Exception {

        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(amount)
                        .setCurrency("usd")
                        .build();

        return PaymentIntent.create(params);
    }
}