package com.bersyte.eventz.features.auth.domain.service;

import com.bersyte.eventz.features.auth.application.events.*;

public interface AuthEventPublisher {
    void publishUserRegistered(UserRegisteredEvent event);
    void publishSendVerificationCode(VerificationCodeResentEvent event);
    void publishUserResetPassword(PasswordResetRequestedEvent event);
    void publishResetPasswordConfirmation(PasswordResetConfirmationEvent event);
    void publishEmailVerificationRequest(VerificationEmailEvent event);
}
