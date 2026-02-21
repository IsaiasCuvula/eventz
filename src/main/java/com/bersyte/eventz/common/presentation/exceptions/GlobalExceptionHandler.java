package com.bersyte.eventz.common.presentation.exceptions;

import com.bersyte.eventz.common.domain.exceptions.BusinessException;
import com.bersyte.eventz.common.domain.exceptions.DatabaseOperationException;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidCredentialsException;
import com.bersyte.eventz.features.auth.domain.exceptions.InvalidVerificationCodeException;
import com.bersyte.eventz.features.events.domain.exceptions.EventNotFoundException;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationAlreadyExistsException;
import com.bersyte.eventz.features.security.JwtAuthenticationException;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationException;
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
import java.util.concurrent.atomic.AtomicReference;


@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(
            EventNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.error("EventNotFoundException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.UNPROCESSABLE_ENTITY);
    }
    
    @ExceptionHandler(EventNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventNotFoundException(
            EventNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.error("EventNotFoundException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.NOT_FOUND);
    }
    
    
    @ExceptionHandler(EventRegistrationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEventRegistrationNotFoundException(
            EventRegistrationNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.error("EventRegistrationNotFoundException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedException(
            UnauthorizedException exception,
            HttpServletRequest request
    ) {
        logger.error("UnauthorizedException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentialsException(
            InvalidCredentialsException exception,
            HttpServletRequest request
    ) {
        logger.error("InvalidVerificationCodeException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(InvalidVerificationCodeException.class)
    public ResponseEntity<ErrorResponse> handleInvalidVerificationCodeException(
            InvalidVerificationCodeException exception,
            HttpServletRequest request
    ) {
        logger.error("InvalidVerificationCodeException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
    
    
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.error("UserNotFoundException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.NOT_FOUND);
    }
    
    
    @ExceptionHandler(EventRegistrationAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEventRegistrationAlreadyExistsException(
            EventRegistrationAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        logger.error("EventRegistrationAlreadyExistsException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }
    
    
    @ExceptionHandler(EventRegistrationException.class)
    public ResponseEntity<ErrorResponse> handleEventRegistrationException(
            EventRegistrationException exception,
            HttpServletRequest request
    ) {
        logger.error("EventRegistrationException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }
    
    @ExceptionHandler(JwtAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(
            JwtAuthenticationException exception,
            HttpServletRequest request
    ) {
        logger.error("JwtAuthenticationException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        //get the default msg defined in the [EventEntity] entity
        AtomicReference<String> defaultMessage = new AtomicReference<>("");
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            defaultMessage.set(objectError.getDefaultMessage());
        }
        logger.error(defaultMessage.toString(), exception);
        return createErrorResponse(request,"Something went wrong",HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseException(
            DatabaseOperationException exception,
            HttpServletRequest request
    ) {
        logger.error("DatabaseOperationException", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(
            AccessDeniedException exception,
            HttpServletRequest request
    ) {
        logger.error("Access Denied Exception", exception);
        return createErrorResponse(request,"Access Denied",HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(
            AuthException exception,
            HttpServletRequest request
    ) {
        logger.error("Auth Exception", exception);
        return createErrorResponse(request,exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException exception,
            HttpServletRequest request
    ) {
        logger.error("Invalid arguments", exception);
        return createErrorResponse(request, "Invalid arguments",HttpStatus.BAD_REQUEST);
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
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
    
    private ResponseEntity<ErrorResponse> createErrorResponse(HttpServletRequest request, String message, HttpStatus status) {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, status);
    }
}