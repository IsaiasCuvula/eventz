package com.bersyte.eventz.events;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record EventRequestDTO(
        @NotBlank(message = "title is required")
        String title,
        @NotBlank(message = "description is required")
        String description,
        @NotBlank(message = "location is required")
        String location,
        @NotNull(message = "date is required")
        Long date,
        Long createdAt
) {
    public EventRequestDTO{
        createdAt = new Date().getTime();
    }
}
