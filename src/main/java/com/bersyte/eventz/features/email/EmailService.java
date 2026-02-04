package com.bersyte.eventz.features.email;

import com.bersyte.eventz.common.presentation.exceptions.AuthException;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }
    
    
    public void sendVerificationEmail(AppUser user) {
        String subject = "Eventz Email Verification Code";
        String verificationCode = user.getVerificationCode();
        String htmlBody = "<html>"
                                  + "<body style=\"font-family: Arial, sans-serif;\">"
                                  + "<div style=\"background-color: #f5f5f5; padding: 20px;\">"
                                  + "<h2 style=\"color: #333;\">Welcome to our app!</h2>"
                                  + "<p style=\"font-size: 16px;\">Please enter the verification code below to continue:</p>"
                                  + "<div style=\"background-color: #fff; padding: 20px; border-radius: 5px; box-shadow: 0 0 10px rgba(0,0,0,0.1);\">"
                                  + "<h3 style=\"color: #333;\">Verification Code:</h3>"
                                  + "<p style=\"font-size: 18px; font-weight: bold; color: #007bff;\">" + verificationCode + "</p>"
                                  + "</div>"
                                  + "</div>"
                                  + "</body>"
                                  + "</html>";
        try {
            this.sendEmail(user.getEmail(), subject, htmlBody);
        } catch (MessagingException e) {
            throw new AuthException(e.getLocalizedMessage());
        }
    }
    
    
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        MimeMessage msg = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body, true);
        mailSender.send(msg);
    }
}
