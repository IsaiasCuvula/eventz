package com.bersyte.eventz.features.registrations.application.events;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.time.LocalDateTime;

public record TicketCancellationEvent(
        String attendeeEmail, String eventTitle, String eventDescription,
        LocalDateTime eventDate, RegistrationStatus status
) {
}
