package org.example.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendTableCreatedEmail(String tableName) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo("capb.caio.becker@gmail.com");
        message.setSubject("Tabela criada com sucesso");
        message.setText(
                "A tabela '" + tableName + "' foi criada com sucesso."
        );

        mailSender.send(message);
    }
}