package com.github.rinnn31.shoppydex.service;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;

@Service
public class MailService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MailService.class);
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${sender.email.address}")
    private String senderAddress;

    public boolean sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(senderAddress);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to send email to %s", to), e);
            return false;
        }
    }

    public boolean sendTemplatedEmail(String to, String subject, String templatePath, Map<String, String> attributes) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        try {
            String template = new String(templateResource.getInputStream().readAllBytes());
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setFrom(senderAddress);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(template, true);
            mailSender.send(mimeMessage);
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("Failed to send email to %s", to), e);
            return false;
        }
    }
}
