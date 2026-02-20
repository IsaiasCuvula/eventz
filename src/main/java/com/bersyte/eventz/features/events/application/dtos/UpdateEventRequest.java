package com.bersyte.eventz.features.events.application.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

public record UpdateEventRequest(
        @Size(min = 3, message = "Title too short")
        String title,
        String description,
        String location,
        @Future(message = "Date must be in the future")
        LocalDateTime date,
        @Min(value = 2, message = "Must have at least 2 participants")
        Integer maxParticipants,
        Integer price
) {
}
