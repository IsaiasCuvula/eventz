package com.bersyte.eventz.features.auth.application.events;

public record PasswordResetRequestedEvent(
        String email,
        String fullName,
        String recoveryCode
) {}
