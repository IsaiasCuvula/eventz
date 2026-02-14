package com.bersyte.eventz.features.notifications.infrastructure.listeners;

import com.bersyte.eventz.features.auth.application.events.*;
import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class AuthNotificationListener {
    private static final Logger logger = LoggerFactory.getLogger(AuthNotificationListener.class);
    
    private final NotificationService notificationService;
    
    public AuthNotificationListener(NotificationService notificationService) {
        this.notificationService = notificationService;
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistered(UserRegisteredEvent event){
        try {
            notificationService.sendVerificationEmail(
                    event.email(),event.fullName(), event.verificationCode()
            );
        } catch (Exception e) {
            logger.error("Could not send welcome email to {}. Reason: {}", event.email(), e.getMessage());
        }
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendVerificationCode(VerificationCodeResentEvent event) {
        try {
            notificationService.sendVerificationEmail(
                event.email(),event.fullName(), event.verificationCode()
            );
        } catch (Exception e) {
            logger.error("Could not send verification email to {}. Reason: {}", event.email(), e.getMessage());
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserResetPassword(PasswordResetRequestedEvent event) {
        try {
            notificationService.sendEmail(
                event.email(), "Reset Password recovery code",
                event.recoveryCode()
            );
        } catch (Exception e) {
            logger.error("Failed to send recovery code: target={}, error={}", event.email(), e.getMessage());
        }
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleResetPasswordConfirmation(PasswordResetConfirmationEvent event) {
        try {
            notificationService.sendEmail(
                    event.email(), "Password Changed Successfully",
                    event.message()
            );
        } catch (Exception e) {
            logger.error("Failed to send password confirmation: target={}, error={}", event.email(), e.getMessage());
        }
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailVerificationRequest(VerificationEmailEvent event) {
        try {
            notificationService.sendVerificationEmail(
                    event.email(),"", event.verificationCode()
            );
        } catch (Exception e) {
            logger.error("FAILED to send Email Verification: target={}, error={}", event.email(), e.getMessage());
        }
    }
}
