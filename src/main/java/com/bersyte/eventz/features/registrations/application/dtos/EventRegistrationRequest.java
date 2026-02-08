package com.bersyte.eventz.features.registrations.application.dtos;

public record EventRegistrationRequest(
        String eventId,String targetUserId, String requesterId
) {
}
