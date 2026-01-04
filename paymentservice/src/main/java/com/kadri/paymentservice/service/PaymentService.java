package com.kadri.paymentservice.service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private PaymentGatewayClient gateway;

    @Retry(name = "paymentGateway")
    @Bulkhead(name = "paymentGateway", type = Bulkhead.Type.THREADPOOL)
    @CircuitBreaker(name = "paymentGateway", fallbackMethod = "paymentFallback")
    @TimeLimiter(name = "paymentGateway")
    public CompletableFuture<Boolean> processPayment(){
        return CompletableFuture.supplyAsync(gateway::charge);
    }

    private CompletableFuture<Boolean> paymentFallback(Throwable exception){
        return CompletableFuture.completedFuture(false);
    }
}
