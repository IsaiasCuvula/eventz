package com.bersyte.eventz.features.registrations.domain.exceptions;

public class EventRegistrationAlreadyExistsException extends RuntimeException {
    public EventRegistrationAlreadyExistsException(String id) {
        super("User Already Join in the event with id: " + id);
    }
}
