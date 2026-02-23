package com.bersyte.eventz.common.domain.exceptions;


public enum ErrorCode {
    // Auth & Users
    USER_NOT_FOUND("user_not_found"),
    INVALID_CREDENTIALS("auth_invalid_credentials"),
    UNAUTHORIZED("auth_unauthorized"),
    ACCESS_DENIED("access_denied"),
    INTERNAL_VERIFICATION_CODE("invalid_verification_code"),
    
    // Events
    EVENT_NOT_FOUND("event_not_found"),
    EVENT_ALREADY_FULL("event_full"),
    EVENT_COUNT_ALREADY_ZERO("event_count_already_zero"),
    
    // Registrations
    REGISTRATION_NOT_FOUND("registration_not_found"),
    REGISTRATION_ALREADY_EXISTS("registration_already_exists"),
    REGISTRATION_INVALID_STATE("registration_invalid_state"),
    
    // System
    INTERNAL_ERROR("internal_server_error"),
    INVALID_ARGUMENTS("invalid_arguments");
    
    private final String value;
    
    ErrorCode(String value) {
        this.value = value;
    }
    
    public String getValue() {
        return value;
    }
}
