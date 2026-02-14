package com.bersyte.eventz.features.auth.application.events;

public record VerificationEmailEvent(
        String email, String verificationCode
) {
}
