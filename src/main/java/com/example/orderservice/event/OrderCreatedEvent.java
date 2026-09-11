package com.example.orderservice.event;

import com.example.orderservice.entity.Status;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderCreatedEvent {

    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    private Status status;
    private String eventType;
    private Long timestamp;

}
