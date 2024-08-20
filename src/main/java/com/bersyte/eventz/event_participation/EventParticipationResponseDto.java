package com.bersyte.eventz.event_participation;

import com.bersyte.eventz.events.EventResponseDto;

import java.util.Date;

public record EventParticipationResponseDto(
        Long id,
        EventResponseDto event,
        String msg,
        Date registrationAt
) {
}
