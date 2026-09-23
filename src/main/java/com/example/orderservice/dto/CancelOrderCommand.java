package com.example.orderservice.dto;

import java.util.UUID;

public record CancelOrderCommand(
        UUID sagaId,
        UUID orderId,
        String reason
) {}