package com.kadri.common.event;

import com.kadri.common.dto.OrderItemDTO;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreatedEvent extends SagaEvent{
    private Long orderId;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;

    public OrderCreatedEvent(String sagaId, Instant timestamp, Long orderId, List<OrderItemDTO> items, BigDecimal totalAmount) {
        super(sagaId, timestamp);
        this.orderId = orderId;
        this.items = items;
        this.totalAmount = totalAmount;
    }
}
