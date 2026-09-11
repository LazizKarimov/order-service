package com.example.orderservice.kafka.producer;

import com.example.orderservice.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {

    private final KafkaTemplate<String, OrderCreatedEvent> kafkaTemplate;

    private static final String TOPIC_NAME = "order-events";

    public void sendOrderEvent(OrderCreatedEvent orderCreatedEvent){

        if (orderCreatedEvent == null) {
            log.warn("Попытка отправить null событие");
            return;
        }

        CompletableFuture<SendResult<String, OrderCreatedEvent>> future =
                kafkaTemplate.send(
                        TOPIC_NAME,
                        orderCreatedEvent.getId().toString(),
                        orderCreatedEvent
                );

        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("OrderEvent отправлен успешно: {}", orderCreatedEvent);
                log.info("   Partition: {}, Offset: {}",
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset());
            } else {
                log.error("Ошибка отправки OrderEvent: {}", orderCreatedEvent, ex);
            }
        });
    }
}
