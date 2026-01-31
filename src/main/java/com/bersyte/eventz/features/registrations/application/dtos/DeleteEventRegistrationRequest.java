package com.bersyte.eventz.features.registrations.application.dtos;

public record DeleteEventRegistrationRequest(
        String registrationId, String requesterEmail
) {
}
