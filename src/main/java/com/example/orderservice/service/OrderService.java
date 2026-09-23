package com.example.orderservice.service;

import com.example.orderservice.dto.CancelOrderCommand;
import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import com.example.orderservice.entity.Status;
import com.example.orderservice.event.OrderCancelledEvent;
import com.example.orderservice.event.OrderCreatedEvent;
import com.example.orderservice.kafka.producer.OrderEventProducer;
import com.example.orderservice.mapper.OrderMapper;
import com.example.orderservice.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.Instant;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer orderEventProducer;

    @Transactional
    public OrderDtoResponse createOrder(OrderDtoRequest request) {

        Order order = new Order();
        order.setCustomerId(request.getCustomerId());
        order.setStatus(Status.NEW);

        BigDecimal total = BigDecimal.ZERO;
        for (OrderDtoRequest.Item i : request.getItems()) {
            OrderItem item = OrderItem.builder()
                    .productId(i.getProductId())
                    .quantity(i.getQuantity())
                    .price(i.getPrice())
                    .build();
            order.addItem(item);

            total = total.add(
                    i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())));
        }
        order.setAmount(total);

        Order savedOrder = orderRepository.save(order);
        log.info("Создан ордер {}", savedOrder.getId());

        OrderCreatedEvent event = OrderCreatedEvent.builder()
                .id(savedOrder.getId())
                .customerId(savedOrder.getCustomerId())
                .amount(savedOrder.getAmount())
                .status(Status.valueOf(savedOrder.getStatus().name()))
                .eventType("ORDER_CREATED")
                .timestamp(System.currentTimeMillis())
                .items(savedOrder.getItems().stream()
                        .map(i -> OrderCreatedEvent.Item.builder()
                                .productId(i.getProductId())
                                .quantity(i.getQuantity())
                                .build())
                        .toList())
                .build();

        orderEventProducer.sendOrderEvent(event);

        return orderMapper.toDto(savedOrder);
    }

    @Transactional
    public void cancelOrder(CancelOrderCommand command) {
        log.info("[Order] Отмена заказа: orderId={}, reason={}",
                command.orderId(), command.reason());

        Order order = orderRepository.findById(command.orderId())
                .orElseThrow(() -> new RuntimeException(
                        "Заказ не найден: " + command.orderId()));

        if (order.getStatus() == Status.CANCELLED) {
            log.warn("[Order] Заказ {} уже отменён, пропускаем", order.getId());
            return;
        }

        order.setStatus(Status.CANCELLED);
        orderRepository.save(order);
        log.info("[Order] Заказ отменён: id={}, status=CANCELLED", order.getId());

        OrderCancelledEvent event = new OrderCancelledEvent(
                command.sagaId(),
                order.getId(),
                Instant.now()
        );
        orderEventProducer.sendOrderCancelledEvent(event);
    }
}
