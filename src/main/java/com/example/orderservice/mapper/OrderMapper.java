package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.entity.Order;
import com.example.orderservice.entity.OrderItem;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDtoResponse toDto(Order order);

    OrderDtoResponse.Item toDto(OrderItem item);
}