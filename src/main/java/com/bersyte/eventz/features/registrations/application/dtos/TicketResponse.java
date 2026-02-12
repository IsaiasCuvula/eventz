package com.bersyte.eventz.features.registrations.application.dtos;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.time.LocalDateTime;

public record TicketResponse(
        String ticketId,
        String attendeeName,
        String attendeeId,
        String eventId,
        String eventTitle,
        String eventDescription,
        String eventLocation,
        LocalDateTime eventDate,
        String checkInToken,
        RegistrationStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
