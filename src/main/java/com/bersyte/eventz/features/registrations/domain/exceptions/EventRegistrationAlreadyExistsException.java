package com.bersyte.eventz.features.registrations.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

import java.util.UUID;

public class EventRegistrationAlreadyExistsException extends DomainException {
    public EventRegistrationAlreadyExistsException(UUID id) {
        super("User Already Join in the event with id: " + id, ErrorCode.REGISTRATION_ALREADY_EXISTS);
    }
}

