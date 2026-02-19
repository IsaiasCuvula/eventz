package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.features.events.domain.model.EventAccessType;
import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID ticketId,
        String attendeeName,
        UUID attendeeId,
        UUID eventId,
        String eventTitle,
        String eventDescription,
        String eventLocation,
        LocalDateTime eventDate,
        String checkInToken,
        RegistrationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        EventAccessType accessType,
        Integer price
) {
}
