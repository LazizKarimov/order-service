package com.example.orderservice.service;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.kafka.producer.OrderEventProducer;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;

    public OrderDtoResponse createOrder(OrderDtoRequest orderDtoRequest){

        Order savedOrder = orderRepository.save(orderMapper.toEntity(orderDtoRequest));

        log.info("Создан ордер {}", savedOrder.getId());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .id(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .amount(savedOrder.getAmount())
                .status(savedOrder.getStatus())
                .eventType("ORDER_CREATED")
                .timestamp(System.currentTimeMillis())
                .build();

        orderEventProducer.sendOrderEvent(event);

        return orderMapper.toDto(savedOrder);
    }
}
