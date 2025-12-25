package com.kadri.common.event;

import com.kadri.common.dto.OrderItemDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreatedEvent extends SagaEvent{
    private Long orderId;
    private List<OrderItemDTO> items;
    private BigDecimal totalAmount;
}
