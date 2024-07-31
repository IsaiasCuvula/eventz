package com.bersyte.eventz.dto;
import java.util.Date;

public record EventRequestDTO(
    String title,
    String description,
    String location,
    Long date,
    Long createdAt
) {
    public EventRequestDTO{
        createdAt = new Date().getTime();
    }
}
