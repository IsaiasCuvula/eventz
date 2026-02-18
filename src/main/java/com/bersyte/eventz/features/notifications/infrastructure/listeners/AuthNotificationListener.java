package com.bersyte.eventz.features.notifications.infrastructure.listeners;

import com.bersyte.eventz.common.infrastructure.utils.SafeActionExecutor;
import com.bersyte.eventz.features.auth.application.events.*;
import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Map;

@Component
public class AuthNotificationListener {
    
    private final NotificationService notificationService;
    private final SafeActionExecutor action;
    
    public AuthNotificationListener(NotificationService notificationService, SafeActionExecutor actionExecutor) {
        this.notificationService = notificationService;
        this.action = actionExecutor;
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserRegistered(UserRegisteredEvent event) {
        var body = Map.of(
                "username ", event.fullName(),
                "message", "Please enter the verification code below to continue:",
                "code", event.fullName()
        );
        
        action.safeExecute(
                () -> notificationService.sendEmail(
                        event.email(),
                        "Welcome to Eventz",
                        "welcome-email",
                        body
                ),
                "welcome email",
                event.email()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserResetPassword(PasswordResetRequestedEvent event) {
        var body = Map.of(
                "username", event.fullName(),
                "message","If you didn't request it just ignore",
                "code", event.recoveryCode()
        );
        
        action.safeExecute(
                () -> notificationService.sendEmail(
                        event.email(),
                        "Reset Password recovery code",
                        "recovery-code",
                        body
                ),
                "recovery code",
                event.email()
        );
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendVerificationCode(VerificationCodeResentEvent event) {
        
        var body = Map.of(
                "username ", event.fullName(),
                "message", "Please enter the verification code below to continue:",
                "code", event.fullName()
        );
        
        action.safeExecute(
                ()->  notificationService.sendEmail(
                        event.email(),
                        "Eventz verification Code",
                        "resend-verification code",
                        body
                ),
                "Send verification code",
                event.email()
        );
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleResetPasswordConfirmation(PasswordResetConfirmationEvent event) {
        var body = Map.of(
                "message",event.message()
        );
        action.safeExecute(
                ()->  notificationService.sendEmail(
                        event.email(),
                        "Password Changed Successfully",
                        "reset-password-confirmation",
                        body
                        
                ),
                "Send Password confirmation",
                event.email()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailVerificationRequest(VerificationEmailEvent event) {
        var body = Map.of(
                "message", "Please enter the verification code below to continue:",
                "code", event.verificationCode()
        );
        
        action.safeExecute(
                ()-> notificationService.sendEmail(
                    event.email(),
                    "Email verification",
                    "email-verification-request",
                    body
                ),
                "Email Verification Request",
                event.email()
        );
    }
}
