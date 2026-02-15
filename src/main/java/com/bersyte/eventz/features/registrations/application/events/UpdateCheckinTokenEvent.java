package com.bersyte.eventz.features.registrations.application.events;

public record UpdateCheckinTokenEvent(
        String attendeeEmail, String eventTitle, String newCheckInToken
 ) {
}
