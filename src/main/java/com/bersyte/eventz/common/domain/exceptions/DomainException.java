package com.bersyte.eventz.common.domain.exceptions;

public abstract class DomainException extends RuntimeException {
    private final ErrorCode errorCode;
    
    protected DomainException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
