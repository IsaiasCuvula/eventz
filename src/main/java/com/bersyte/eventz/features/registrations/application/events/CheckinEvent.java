package com.bersyte.eventz.features.registrations.application.events;

import java.time.LocalDateTime;

public record CheckinEvent(
        String attendeeEmail,  String eventTitle, String eventLocation,
        LocalDateTime checkInDate
) {
}
