package com.bersyte.eventz.features.events.application.dtos;

public record UpdateEventInput(
        UpdateEventRequest request,
        String id
) {
}
