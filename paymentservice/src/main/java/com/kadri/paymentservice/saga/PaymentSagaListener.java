package com.kadri.paymentservice.saga;

import com.kadri.common.enums.PaymentStatus;
import com.kadri.common.event.InventoryReservedEvent;
import com.kadri.common.event.PaymentFailedEvent;
import com.kadri.common.event.PaymentProcessedEvent;
import com.kadri.paymentservice.entity.Payment;
import com.kadri.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class PaymentSagaListener {

    private final PaymentRepository repository;
    private final StreamBridge streamBridge;

    @KafkaListener(topics = "inventory-reserved")
    public void handleInventoryReserved(InventoryReservedEvent event){
        boolean success = simulatePayment();

        Payment payment = Payment.builder()
                .orderId(event.getOrderId())
                .amount(BigDecimal.ZERO)
                .createdAt(Instant.now())
                .build();

        if(success){
            payment.setStatus(PaymentStatus.SUCCESS);
            repository.save(payment);
            streamBridge.send("paymentProcessed-out-0",
                    new PaymentProcessedEvent(
                            event.getSagaId(),
                            Instant.now(),
                            event.getOrderId()
                    )
            );
        }else {
            payment.setStatus(PaymentStatus.FAILED);
            repository.save(payment);
            streamBridge.send("paymentFailed-out-0",
                    new PaymentFailedEvent(
                            event.getSagaId(),
                            Instant.now(),
                            event.getOrderId(),
                            "PAYMENT_DECLINED"
                    ));
        }
    }

    private boolean simulatePayment() {
        return Math.random() > 0.3; // 70% success
    }
}
