package com.example.exception;

import com.example.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

//    TODO: handle and send appropriate response

    private static final String DEFAULT_FIELD_NAME = "errorMessage";

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        log.error("EmailAlreadyExistsException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.CONFLICT, "EMAIL_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUsernameAlreadyExistsException(UsernameAlreadyExistsException ex) {
        log.error("UsernameAlreadyExistsException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.CONFLICT, "USERNAME_ALREADY_EXISTS", ex.getMessage());
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleBookNotFoundException(BookNotFoundException ex) {
        log.error("BookNotFoundException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(RatingNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRatingNotFoundException(RatingNotFoundException ex) {
        log.error("RatingNotFoundException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.NOT_FOUND, "RATING_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(UserBookNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserBookNotFoundException(UserBookNotFoundException ex) {
        log.error("UserBookNotFoundException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.NOT_FOUND, "USER_BOOK_NOT_FOUND", ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
        log.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName;
            if (error instanceof FieldError fieldError) fieldName = fieldError.getField();
            else fieldName = DEFAULT_FIELD_NAME;
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        log.warn("Validation errors: {}", errors);
        return ErrorResponse.build(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED","validation failed", errors);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        log.error("ValidationException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", ex.getMessage());
    }

    @ExceptionHandler(AppAuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAppAuthenticationException(AppAuthenticationException ex) {
        log.error("AppAuthenticationException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.UNAUTHORIZED, "AUTHENTICATION_FAILED");
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
        log.error("AuthenticationException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", ex.getMessage());
    }

    @ExceptionHandler(InvalidJwtTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidJwtTokenException(InvalidJwtTokenException ex) {
        log.error("InvalidJwtTokenException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
    }
}
