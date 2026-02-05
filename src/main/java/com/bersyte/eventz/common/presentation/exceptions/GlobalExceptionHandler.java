package com.bersyte.eventz.common.presentation.exceptions;

import com.bersyte.eventz.common.domain.exceptions.DatabaseOperationException;
import com.bersyte.eventz.features.auth.domain.exceptions.AuthException;
import com.bersyte.eventz.features.registrations.domain.exceptions.EventRegistrationException;
import jakarta.servlet.http.HttpServletRequest;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        //get the default msg defined in the [EventEntity] entity
        AtomicReference<String> defaultMessage = new AtomicReference<>("");
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            defaultMessage.set(objectError.getDefaultMessage());
        }

        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                defaultMessage.toString(),
                exception.getStatusCode().value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ErrorResponse> handleDatabaseExceptions(
            DatabaseOperationException e,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> accessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EventRegistrationException.class)
    public ResponseEntity<ErrorResponse> eventRegistrationException(
            EventRegistrationException e,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> authException(
            AuthException e,
            HttpServletRequest request
    ) {
        ErrorResponse error = new ErrorResponse(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}