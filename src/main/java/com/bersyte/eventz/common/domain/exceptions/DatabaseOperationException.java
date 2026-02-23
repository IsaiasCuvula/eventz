package com.bersyte.eventz.common.domain.exceptions;

public class DatabaseOperationException extends DomainException {

    public DatabaseOperationException(String message) {
        super(message, ErrorCode.INTERNAL_ERROR);
    }
}