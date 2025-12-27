package com.kadri.orderservice.service;

import com.kadri.common.dto.CreateOrderRequest;
import com.kadri.common.enums.OrderStatus;
import com.kadri.common.event.OrderCreatedEvent;
import com.kadri.orderservice.entity.Order;
import com.kadri.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final StreamBridge streamBridge;

    public Long create(CreateOrderRequest request) {
        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .totalAmount(request.getTotalAmount())
                .status(OrderStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        repository.save(order);

        OrderCreatedEvent event = new OrderCreatedEvent(
                UUID.randomUUID().toString(),
                Instant.now(),
                order.getId(),
                request.getItems(),
                request.getTotalAmount()
        );

        streamBridge.send("orderCreated-out-0", event);
        return order.getId();
    }
}
