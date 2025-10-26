package com.github.rinnn31.shoppydex.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

@Service
public class MailService {
    @Autowired
    private MailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendTemplatedEmail(String to, String subject, String templatePath, Map<String, String> attributes) {
        ClassPathResource templateResource = new ClassPathResource(templatePath);
        try {
            String template = new String(templateResource.getInputStream().readAllBytes());
            for (Map.Entry<String, String> entry : attributes.entrySet()) {
                template = template.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }
            sendEmail(to, subject, template);
        } catch (IOException e) {
            /* ignored */
        }
    }
}
