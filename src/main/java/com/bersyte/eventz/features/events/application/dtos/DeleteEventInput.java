package com.bersyte.eventz.features.events.application.dtos;

public record DeleteEventInput(
        String eventId, String requesterId
) {
}
