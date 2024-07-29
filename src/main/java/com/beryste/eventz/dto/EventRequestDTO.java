package com.beryste.eventz.dto;

public record EventRequestDTO(
    String title,
    String description,
    String local,
    Long date,
    Long createdAt
) {}
