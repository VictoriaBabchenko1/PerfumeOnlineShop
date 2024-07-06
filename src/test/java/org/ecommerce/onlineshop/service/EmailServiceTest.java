package org.ecommerce.onlineshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class EmailServiceTest {
    @Mock
    private JavaMailSender javaMailSender;

    @Test
    void sendSimpleMessageTest() {
        EmailService emailService = new EmailService(javaMailSender);
        String to = "test@example.com";
        String subject = "Test Subject";
        String text = "Test Message";

        emailService.sendSimpleMessage(to, subject, text);

        verify(javaMailSender).send(any(SimpleMailMessage.class));
    }
}
