package com.bersyte.eventz.event_participation;

import com.bersyte.eventz.events.EventResponseDTO;

import java.util.Date;

public record EventParticipationResponseDTO(
        Long id,
        EventResponseDTO event,
        String msg,
        Date registrationAt
) {
}
