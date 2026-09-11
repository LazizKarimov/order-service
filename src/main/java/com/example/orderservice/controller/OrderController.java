package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequestMapping("/orders")
@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/create")
    public OrderDtoResponse createOrder(@RequestBody OrderDtoRequest orderDto) {
        log.info("Запрос на создания ордера {}", orderDto.getCustomerId());

        OrderDtoResponse order = orderService.createOrder(orderDto);

        log.info("Создан заказ {}", order.getId());
        return order;

    }
}
