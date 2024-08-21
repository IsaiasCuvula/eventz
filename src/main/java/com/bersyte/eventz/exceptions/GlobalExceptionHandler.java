package com.bersyte.eventz.exceptions;

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
    public ResponseEntity<ApiError> handleValidationExceptions(
            MethodArgumentNotValidException exception,
            HttpServletRequest request
    ){
        //get the default msg defined in the [Event] entity
        AtomicReference<String> defaultMessage = new AtomicReference<>("");
        for (ObjectError objectError : exception.getBindingResult().getAllErrors()) {
            defaultMessage.set(objectError.getDefaultMessage());
        }

        ApiError error = new ApiError(
                request.getRequestURI(),
                defaultMessage.toString(),
                exception.getStatusCode().value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(DatabaseOperationException.class)
    public ResponseEntity<ApiError> handleDatabaseExceptions(
            DatabaseOperationException e,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> accessDeniedException(
            AccessDeniedException e,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(EventRegistrationException.class)
    public ResponseEntity<ApiError> eventRegistrationException(
            EventRegistrationException e,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ApiError> authException(
            AuthException e,
            HttpServletRequest request
    ) {
        ApiError error = new ApiError(
                request.getRequestURI(),
                e.getLocalizedMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}