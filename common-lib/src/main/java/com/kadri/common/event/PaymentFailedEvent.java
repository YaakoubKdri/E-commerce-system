package com.kadri.common.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.Instant;

@Data
@EqualsAndHashCode(callSuper = true)
public class PaymentFailedEvent extends SagaEvent{
    private Long orderId;
    private String reason;

    public PaymentFailedEvent(String sagaId, Instant timestamp, Long orderId, String reason) {
        super(sagaId, timestamp);
        this.orderId = orderId;
        this.reason = reason;
    }
}
