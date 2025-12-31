package com.kadri.common.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentProcessedEvent extends SagaEvent{
    private Long orderId;

    public PaymentProcessedEvent(String sagaId, Instant timestamp, Long orderId) {
        super(sagaId, timestamp);
        this.orderId = orderId;
    }
}
