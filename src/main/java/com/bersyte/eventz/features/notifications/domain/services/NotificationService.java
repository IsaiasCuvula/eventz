package com.bersyte.eventz.features.notifications.domain.services;

import java.util.Map;

public interface NotificationService {
     void sendEmail(String to, String subject, String templateName, Map<String, String> bodyMap) throws Exception;
}
