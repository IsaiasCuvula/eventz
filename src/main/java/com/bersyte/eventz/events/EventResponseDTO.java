package com.bersyte.eventz.events;

import java.util.Date;
import java.util.List;


public record EventResponseDTO(
    Integer id,
    String title,
    String description,
    String location,
    Date date,
    String organizer,
    List<String> participants,
    Date createdAt
) {}
