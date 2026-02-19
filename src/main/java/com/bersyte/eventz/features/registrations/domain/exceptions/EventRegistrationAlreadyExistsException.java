package com.bersyte.eventz.features.registrations.domain.exceptions;

import java.util.UUID;

public class EventRegistrationAlreadyExistsException extends RuntimeException {
    public EventRegistrationAlreadyExistsException(UUID id) {
        super("User Already Join in the event with id: " + id);
    }
}
