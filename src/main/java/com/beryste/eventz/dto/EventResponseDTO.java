package com.beryste.eventz.dto;
import java.util.Date;


public record EventResponseDTO(
    Integer id,
    String title,
    String description,
    String location,
    Date date,
    Date createdAt
) {}
