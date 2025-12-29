package com.kadri.inventoryservice.saga;

import com.kadri.common.dto.OrderItemDTO;
import com.kadri.common.event.InventoryReleasedEvent;
import com.kadri.common.event.InventoryReservedEvent;
import com.kadri.common.event.OrderCreatedEvent;
import com.kadri.common.event.PaymentFailedEvent;
import com.kadri.inventoryservice.entity.Inventory;
import com.kadri.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.net.http.HttpClient;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class InventorySagaListener {
    private final InventoryRepository repository;
    private final StreamBridge streamBridge;

    @KafkaListener(topics = "order-created")
    public void handleOrderCreated(OrderCreatedEvent event){
        for(OrderItemDTO itemDTO : event.getItems()){
            Inventory inventory = repository.findById(itemDTO.getProductId())
                    .orElseThrow(() -> new IllegalStateException("Product not found."));
            if(inventory.getAvailableQuantity() < itemDTO.getQuantity()){
                throw new IllegalStateException("Insufficient stock.");
            }

            inventory.setAvailableQuantity(
                    inventory.getAvailableQuantity() - itemDTO.getQuantity()
            );

            repository.save(inventory);
        }
        InventoryReservedEvent reservedEvent =
                new InventoryReservedEvent(
                        event.getSagaId(),
                        Instant.now(),
                        event.getOrderId()
                );
        streamBridge.send("InventoryReserved-out-0", reservedEvent);
    }

    @KafkaListener(topics = "payment-failed")
    public void handlePaymentFailed(PaymentFailedEvent event){
        // In a real system we'd persist reservation details.
        // For now, assume we can restore quantities.

        InventoryReleasedEvent releasedEvent =
                new InventoryReleasedEvent(
                        event.getSagaId(),
                        Instant.now(),
                        event.getOrderId()
                );
        streamBridge.send("inventoryReleased-out-0", releasedEvent);
    }
}
