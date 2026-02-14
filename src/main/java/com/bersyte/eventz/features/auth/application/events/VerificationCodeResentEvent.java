package com.bersyte.eventz.features.auth.application.events;

public record VerificationCodeResentEvent(
        String email,
        String fullName,
        String verificationCode
) {
}
