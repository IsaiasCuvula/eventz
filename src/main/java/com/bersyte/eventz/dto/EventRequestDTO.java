package com.bersyte.eventz.dto;
import java.util.Date;
import jakarta.validation.constraints.*;

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
