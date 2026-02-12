package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.time.LocalDateTime;

public record EventParticipantResponse(
        String registrationId,
        String eventId,
        String eventTitle,
        String eventDescription,
        LocalDateTime eventDate,
        String userId,
        String username,
        String email,
        RegistrationStatus status,
        LocalDateTime joinedAt,
        LocalDateTime updatedAt
) {
}
