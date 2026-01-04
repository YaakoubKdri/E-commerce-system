package com.kadri.paymentservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Entity
@Table(name = "processed_event")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProcessedEvent {
    @Id
    private String eventId;
    private Instant processedAt;
}
//CREATE TABLE processed_event (
//        event_id VARCHAR(100) PRIMARY KEY,
//processed_at TIMESTAMP NOT NULL
//);
