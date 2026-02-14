package com.bersyte.eventz.features.auth.application.events;

public record PasswordResetConfirmationEvent(
        String email,
        String message
) {
}
