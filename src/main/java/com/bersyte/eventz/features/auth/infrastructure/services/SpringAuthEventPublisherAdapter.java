package com.bersyte.eventz.features.auth.infrastructure.services;

import com.bersyte.eventz.features.auth.application.events.*;
import com.bersyte.eventz.features.auth.domain.service.AuthEventPublisher;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringAuthEventPublisherAdapter implements AuthEventPublisher {
    
    private final ApplicationEventPublisher publisher;
    
    public SpringAuthEventPublisherAdapter(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
    
    
    @Override
    public void publishUserRegistered(UserRegisteredEvent event) {
        publisher.publishEvent(event);
    }
    
    @Override
    public void publishSendVerificationCode(VerificationCodeResentEvent event) {
        publisher.publishEvent(event);
    }
    
    @Override
    public void publishUserResetPassword(PasswordResetRequestedEvent event) {
        publisher.publishEvent(event);
    }
    
    @Override
    public void publishResetPasswordConfirmation(PasswordResetConfirmationEvent event) {
        publisher.publishEvent(event);
    }
    
    @Override
    public void publishEmailVerificationRequest(VerificationEmailEvent event) {
        publisher.publishEvent(event);
    }
}
