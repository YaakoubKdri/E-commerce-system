package com.kadri.orderservice.service;

import com.kadri.common.dto.CreateOrderRequest;
import com.kadri.common.enums.OrderStatus;
import com.kadri.common.event.OrderCreatedEvent;
import com.kadri.orderservice.entity.Order;
import com.kadri.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository repository;
    private final StreamBridge streamBridge;
    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

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

        boolean sent = streamBridge.send("orderCreated-out-0", event);
        log.info("Event sent? {}", sent);
        return order.getId();
    }
}
