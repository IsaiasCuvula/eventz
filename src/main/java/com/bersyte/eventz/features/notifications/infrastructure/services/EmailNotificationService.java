package com.bersyte.eventz.features.notifications.infrastructure.services;

import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
public class EmailNotificationService implements NotificationService {
    private final JavaMailSender mailSender;
    private final EmailTemplateProcessor templateProcessor;
    
    public EmailNotificationService(JavaMailSender mailSender, EmailTemplateProcessor templateProcessor) {
        this.mailSender = mailSender;
        this.templateProcessor = templateProcessor;
    }
    
    @Override
    public void sendEmail(String to, String subject, String templateName, Map<String, String> bodyMap) throws Exception {
        String htmlContent  = templateProcessor.process(templateName,bodyMap);
        
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true, "UTF-8");
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        mailSender.send(msg);
    }
}
