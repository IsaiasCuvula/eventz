package com.bersyte.eventz.features.notifications.domain.services;

public interface NotificationService {
    void sendVerificationEmail(String email, String name, String code) throws Exception;
    void sendEmail(String to, String subject, String body) throws Exception;
}
