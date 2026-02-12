package com.bersyte.eventz.features.events.application.dtos;

public record EventByIdInput(
        String userEmail,
        String eventId
) {
}
