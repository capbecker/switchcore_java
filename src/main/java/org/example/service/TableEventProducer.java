package org.example.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@Service
public class TableEventProducer {

    private final RabbitTemplate rabbitTemplate;

    public TableEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void tableCreated(String tableName) {
        rabbitTemplate.convertAndSend("table-created", tableName);
    }
}