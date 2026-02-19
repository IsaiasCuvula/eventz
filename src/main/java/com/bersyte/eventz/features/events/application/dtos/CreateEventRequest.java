package com.bersyte.eventz.features.events.application.dtos;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record CreateEventRequest(
        @NotBlank(message = "title is required")
        String title,
        @NotBlank(message = "description is required")
        String description,
        @NotBlank(message = "location is required")
        String location,
        @NotNull(message = "date is required")
        @Future(message = "Event date must be in the future")
        LocalDateTime date,
        @NotNull(message = "Max participants is required")
        @Min(value = 2, message = "Must have at least 2 participant")
        Integer maxParticipants,
        Integer price
) {}
