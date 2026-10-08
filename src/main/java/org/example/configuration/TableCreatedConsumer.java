package org.example.configuration;

import org.example.service.EmailService;
import org.springframework.stereotype.Component;

import org.springframework.amqp.rabbit.annotation.RabbitListener;

@Component
public class TableCreatedConsumer {

    private final EmailService emailService;

    public TableCreatedConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "table-created")
    public void process(String tableName) {
        emailService.sendTableCreatedEmail(tableName);
    }
}