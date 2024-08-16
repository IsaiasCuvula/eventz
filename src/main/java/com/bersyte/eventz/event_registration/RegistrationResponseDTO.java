package com.bersyte.eventz.event_registration;

import com.bersyte.eventz.events.EventResponseDTO;

import java.util.Date;

public record RegistrationResponseDTO(
        Long id,
        EventResponseDTO event,
        String msg,
        Date registrationAt
) {
}
