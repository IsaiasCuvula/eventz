package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.features.events.domain.model.EventAccessType;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record EventParticipantResponse(
        UUID registrationId,
        UUID eventId,
        String eventTitle,
        String eventDescription,
        LocalDateTime eventDate,
        UUID userId,
        String username,
        String email,
        RegistrationStatus status,
        LocalDateTime joinedAt,
        LocalDateTime updatedAt,
        EventAccessType accessType,
        Integer price
) {
}
