package com.kadri.orderservice.controller;

import com.kadri.common.dto.CreateOrderRequest;
import com.kadri.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;

    @PostMapping
    public ResponseEntity<Long> createOrder(@RequestBody @Valid CreateOrderRequest request){
        Long orderId = service.create(request);
        return ResponseEntity.accepted().body(orderId);
    }
}
