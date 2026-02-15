package com.bersyte.eventz.features.registrations.application.events;

import com.bersyte.eventz.features.registrations.domain.model.RegistrationStatus;

import java.time.LocalDateTime;

public record EventRegistrationEvent(
        String attendeeEmail,
        String attendeeName,
        String eventTitle,
        String eventDescription,
        String eventLocation,
        LocalDateTime eventDate,
        String checkInToken,
        RegistrationStatus status
) {
}
