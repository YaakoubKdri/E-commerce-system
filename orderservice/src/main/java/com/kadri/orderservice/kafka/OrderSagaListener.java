package com.kadri.orderservice.kafka;

import com.kadri.common.enums.OrderStatus;
import com.kadri.common.event.PaymentFailedEvent;
import com.kadri.common.event.PaymentProcessedEvent;
import com.kadri.orderservice.entity.Order;
import com.kadri.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderSagaListener {

    private final OrderRepository repository;

    @KafkaListener(topics = "payment-processed")
    public void onPaymentProcessed(PaymentProcessedEvent event){
        Order order = repository.findById(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.PAYMENT_COMPLETED);
        repository.save(order);
    }

    @KafkaListener(topics = "payment-failed")
    public void onPaymentFailed(PaymentFailedEvent event){
        Order order = repository.findById(event.getOrderId()).orElseThrow();
        order.setStatus(OrderStatus.CANCELED);
        repository.save(order);
    }
}
