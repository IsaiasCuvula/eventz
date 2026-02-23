package com.bersyte.eventz.features.events.domain.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DomainException;
import com.bersyte.eventz.common.domain.exceptions.ErrorCode;

public class InvalidEventOperationException extends DomainException {
    public InvalidEventOperationException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
