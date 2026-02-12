package com.bersyte.eventz.features.registrations.application.dtos;

public record CancelEventRegistrationRequest(
        String registrationId, String requesterId
) {
}
