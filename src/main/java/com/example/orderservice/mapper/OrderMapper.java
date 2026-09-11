package com.example.orderservice.mapper;

import com.example.orderservice.dto.OrderDtoRequest;
import com.example.orderservice.dto.OrderDtoResponse;
import com.example.orderservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface OrderMapper {

    OrderDtoResponse toDto(Order order);

    @Mapping(target = "id", ignore = true)
    Order toEntity(OrderDtoRequest orderDtoRequest);
}
