package com.bersyte.eventz.features.registrations.domain.exceptions;

public class EventRegistrationNotFoundException extends  RuntimeException{
    public EventRegistrationNotFoundException(String message) {
        super(message);
    }
}
