package com.bersyte.eventz.features.events.application.dtos;

public record CreateEventInput(
        String userEmail,
        CreateEventRequest request
) {}
