package com.kadri.paymentservice.service;

import org.springframework.stereotype.Component;

@Component
public class PaymentGatewayClient {
    public boolean charge() {
        try{
            Thread.sleep(800);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return Math.random() > 0.3;
    }
}
