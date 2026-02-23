package com.bersyte.eventz.features.registrations.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class EventAlreadyFullException extends DomainException {
    public EventAlreadyFullException(String message) {
        super(message, ErrorCode.EVENT_ALREADY_FULL);
    }
}
