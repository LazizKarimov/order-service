package com.example.orderservice.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDtoRequest {

    private UUID customerId;

    private List<Item> items;

    @Data
    public static class Item {
        private String productId;
        private int quantity;
        private BigDecimal price;
    }
}