package com.bersyte.eventz.exceptions;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ApiError> handleDatabaseExceptions(DatabaseOperationException e) {
        ApiError error = new ApiError(
                "eventz/",
                e.getLocalizedMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}