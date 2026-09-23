package com.example.orderservice.kafka.consumer;

import com.example.orderservice.dto.CancelOrderCommand;
import com.example.orderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
@Slf4j
public class CancelOrderCommandConsumer {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    @KafkaListener(
            topics = "order-commands",
            groupId = "${spring.kafka.consumer.group-id:order-service-group}",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeCancelOrderCommand(ConsumerRecord<String, String> record, Acknowledgment ack) {
        log.info("[Order] Получен CancelOrderCommand: {}", record.value());
        try {
            CancelOrderCommand command =
                    objectMapper.readValue(record.value(), CancelOrderCommand.class);
            orderService.cancelOrder(command);
            ack.acknowledge();
            log.info("[Order] CancelOrderCommand подтверждён: orderId={}", command.orderId());
        } catch (Exception e) {
            log.error("Ошибка обработки CancelOrderCommand: {}", record.value(), e);
            // не ack — Kafka перечитает
        }
    }
}