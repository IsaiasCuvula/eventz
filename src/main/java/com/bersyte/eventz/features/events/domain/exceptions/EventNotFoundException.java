package com.bersyte.eventz.features.events.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

import java.util.UUID;

public class EventNotFoundException extends DomainException {
    public EventNotFoundException(UUID id) {
        super("Event not found with ID: " + id, ErrorCode.EVENT_NOT_FOUND);
    }
}