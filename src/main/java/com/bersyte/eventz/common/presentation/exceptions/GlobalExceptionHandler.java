package com.bersyte.eventz.common.presentation.exceptions;

import com.bersyte.eventz.common.domain.exceptions.*;
import com.bersyte.eventz.common.presentation.dtos.ErrorResponse;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidCredentialsException;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidVerificationCodeException;
import com.bersyte.eventz.features.events.domain.exceptions.EventNotFoundException;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationAlreadyExistsException;
import com.bersyte.eventz.features.registrations.domain.exceptions.InvalidRegistrationStateException;
import com.bersyte.eventz.features.security.exceptions.JwtAuthenticationException;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationNotFoundException;
import com.bersyte.eventz.features.users.domain.exception.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    
    
    @ExceptionHandler(InvalidRegistrationStateException.class)
    public ResponseEntity<ErrorResponse> handleEventRegistrationNotFoundException(
            InvalidRegistrationStateException exception,
            HttpServletRequest request
    ) {
        logger.warn("Invalid Event Registration State", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.BAD_REQUEST,
                exception.getErrorCode()
        );
    }
    
    
    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(DomainException exception, HttpServletRequest request) {
        logger.error("Business rule violation", exception);
        return createErrorResponse(
                request, exception.getMessage(), HttpStatus.BAD_REQUEST,
                exception.getErrorCode()
        );
    }
    
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(
            EventNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.warn("Event Not Found", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.NOT_FOUND,
                exception.getErrorCode()
        );
    }
    
    
    @ExceptionHandler(EventRegistrationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventRegistrationNotFoundException(
            EventRegistrationNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.warn("Event Registration Not Found", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.NOT_FOUND,
                exception.getErrorCode()
        );
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(UnauthorizedException exception, HttpServletRequest request) {
        logger.error("Permission denied", exception);
        return createErrorResponse(
                request, exception.getMessage(), HttpStatus.FORBIDDEN,
                exception.getErrorCode()
        );
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException exception,
            HttpServletRequest request
    ) {
        logger.error("Invalid Credentials", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.BAD_REQUEST,
                exception.getErrorCode()
        );
    }
    
    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidVerificationCodeException(
            InvalidVerificationCodeException exception,
            HttpServletRequest request
    ) {
        logger.error("Invalid Verification Code", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.BAD_REQUEST,
                exception.getErrorCode()
        );
    }
    
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.warn("User Not Found", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.NOT_FOUND,
                exception.getErrorCode()
        );
    }
    
    
    @ExceptionHandler(EventRegistrationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventRegistrationAlreadyExistsException(EventRegistrationAlreadyExistsException exception, HttpServletRequest request) {
        logger.error("Registration conflict", exception);
        return createErrorResponse(
                request, exception.getMessage(), HttpStatus.CONFLICT,
                exception.getErrorCode()
        );
    }
    
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(
            JwtAuthenticationException exception,
            HttpServletRequest request
    ) {
        logger.error("Jwt Authentication Exception", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.UNAUTHORIZED,
                exception.getErrorCode()
        );
    }
    
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(
            AuthException exception,
            HttpServletRequest request
    ) {
        logger.error("Auth Exception", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.BAD_REQUEST,
                exception.getErrorCode()
        );
    }
    
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        String errorMessage = exception.getBindingResult()
                                      .getFieldErrors()
                                      .stream()
                                      .map(ObjectError::getDefaultMessage)
                                      .findFirst()
                                      .orElse("Validation failed");
        
        logger.error("Validation error: {}", errorMessage);
        return createErrorResponse(
                request,errorMessage,HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENTS
        );
    }


    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            DatabaseOperationException exception,
            HttpServletRequest request
    ) {
        logger.error("Database Operation Exception", exception);
        return createErrorResponse(
                request,exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR,
                exception.getErrorCode()
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        logger.error("Access Denied Exception", exception);
        return createErrorResponse(
                request,"Access Denied",HttpStatus.FORBIDDEN,
                ErrorCode.ACCESS_DENIED
        );
    }

    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        logger.error("Invalid arguments", exception);
        return createErrorResponse(
                request, "Invalid arguments",HttpStatus.BAD_REQUEST,
                ErrorCode.INVALID_ARGUMENTS
        );
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGlobalException(
            Exception exception,
            HttpServletRequest request
    ) {
        logger.error("Unexpected error: ", exception);
        
        return createErrorResponse(
                request,
                "An unexpected error occurred. Please contact support.",
                HttpStatus.INTERNAL_SERVER_ERROR,
                ErrorCode.INTERNAL_ERROR
        );
    }
    
    private ResponseEntity<ErrorResponse> createErrorResponse(
            HttpServletRequest request, String message, HttpStatus status,
            ErrorCode errorCode
    ) {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                message,
                errorCode.getValue(),
                status.value(),
                LocalDateTime.now()
              
        );
        return new ResponseEntity<>(error, status);
    }
}