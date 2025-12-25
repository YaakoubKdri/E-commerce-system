package com.kadri.common.event;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class InventoryReleasedEvent extends SagaEvent{
    private Long orderId;
}
