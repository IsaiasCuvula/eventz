package com.bersyte.eventz.features.event_participation;

import com.bersyte.eventz.features.events.EventResponseDto;

import java.util.Date;

public record EventParticipationResponseDto(
        Long id,
        EventResponseDto event,
        String msg,
        Date registrationAt
) {
}
