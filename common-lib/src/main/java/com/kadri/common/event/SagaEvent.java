package com.kadri.common.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@AllArgsConstructor
@Data
public abstract class SagaEvent {
    private String sagaId;
    private Instant timestamp;
}
