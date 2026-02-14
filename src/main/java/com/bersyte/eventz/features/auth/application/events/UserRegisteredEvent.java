package com.bersyte.eventz.features.auth.application.events;

public record UserRegisteredEvent(
        String email,
        String fullName,
        String verificationCode
) {}