package com.example.orderservice.dto;

import com.example.orderservice.entity.Status;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class OrderDtoResponse {

    private UUID id;

    private UUID customerId;

    private BigDecimal amount;

    private Status status;
}
