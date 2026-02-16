package com.bersyte.eventz.features.notifications.infrastructure.listeners;

import com.bersyte.eventz.common.infrastructure.utils.SafeActionExecutor;
import com.bersyte.eventz.features.auth.application.events.*;
import com.bersyte.eventz.features.notifications.domain.services.NotificationService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
        action.safeExecute(
                () -> notificationService.sendVerificationEmail(event.email(), event.fullName(), event.verificationCode()),
                "welcome email",
                event.email()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleUserResetPassword(PasswordResetRequestedEvent event) {
        action.safeExecute(
                () -> notificationService.sendEmail(event.email(), "Reset Password recovery code", event.recoveryCode()),
                "recovery code",
                event.email()
        );
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSendVerificationCode(VerificationCodeResentEvent event) {
        action.safeExecute(
                ()->  notificationService.sendVerificationEmail(
                        event.email(),event.fullName(), event.verificationCode()
                ),
                "Send verification code",
                event.email()
        );
    }
    
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleResetPasswordConfirmation(PasswordResetConfirmationEvent event) {
        action.safeExecute(
                ()->  notificationService.sendEmail(
                        event.email(), "Password Changed Successfully",
                        event.message()
                ),
                "Send Password confirmation",
                event.email()
        );
    }
    
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleEmailVerificationRequest(VerificationEmailEvent event) {
       action.safeExecute(
                ()-> notificationService.sendVerificationEmail(
                    event.email(),"", event.verificationCode()),
                    "Email verification",
                    event.email()
        );
    }
}
