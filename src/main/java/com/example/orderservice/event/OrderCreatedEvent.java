package com.example.orderservice.event;

import com.example.orderservice.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderCreatedEvent {

    private UUID id;
    private UUID customerId;
    private BigDecimal amount;
    private Status status;
    private String eventType;
    private Long timestamp;

    private List<Item> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Item {
        private String productId;
        private int quantity;
    }

}
