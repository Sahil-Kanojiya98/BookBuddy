package com.example.exception;

import com.example.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	// TODO: handle and send appropriate response

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

	@ExceptionHandler(UserBookAlreadyExistsException.class)
	public ResponseEntity<ErrorResponse> handleUserBookAlreadyExistsException(UserBookAlreadyExistsException ex) {
		log.error("UserBookAlreadyExistsException: {}", ex.getMessage(), ex);
		return ErrorResponse.build(HttpStatus.NOT_FOUND, "USER_BOOK_ALREADY_EXISTS", ex.getMessage());
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
		log.error("UserNotFoundException: {}", ex.getMessage(), ex);
		return ErrorResponse.build(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", ex.getMessage());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex) {
		log.error("MethodArgumentNotValidException: {}", ex.getMessage(), ex);

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String fieldName;
			if (error instanceof FieldError fieldError)
				fieldName = fieldError.getField();
			else
				fieldName = DEFAULT_FIELD_NAME;
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});

		log.warn("Validation errors: {}", errors);
		return ErrorResponse.build(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "validation failed", errors);
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

    @ExceptionHandler(RateLimitExceededException.class)
    public ResponseEntity<ErrorResponse> handleRateLimitExceededException(RateLimitExceededException ex) {
        log.error("RateLimitExceededException: {}", ex.getMessage(), ex);
        return ErrorResponse.build(HttpStatus.TOO_MANY_REQUESTS, "RATE_LIMIT_EXCEEDED", ex.getMessage());
    }

	@ExceptionHandler(InvalidJwtTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidJwtTokenException(InvalidJwtTokenException ex) {
		log.error("InvalidJwtTokenException: {}", ex.getMessage(), ex);
		return ErrorResponse.build(HttpStatus.UNAUTHORIZED, "INVALID_TOKEN");
	}

	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErrorResponse> handleNoResourceFound(NoResourceFoundException ex) {
		log.warn("NoResourceFoundException: {}", ex.getMessage());
		return ErrorResponse.build(HttpStatus.NOT_FOUND, "RESOURCE_NOT_FOUND");
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
		log.warn("HttpRequestMethodNotSupportedException: {}", ex.getMessage());
		return ErrorResponse.build(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", ex.getMessage());
	}

	@ExceptionHandler(MissingServletRequestParameterException.class)
	public ResponseEntity<ErrorResponse> handleMissingParam(MissingServletRequestParameterException ex) {
		log.warn("MissingServletRequestParameterException: {}", ex.getMessage());
		return ErrorResponse.build(HttpStatus.BAD_REQUEST, "MISSING_REQUEST_PARAMETER", ex.getMessage());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleUnreadableMessage(HttpMessageNotReadableException ex) {
		log.warn("HttpMessageNotReadableException: {}", ex.getMessage());
		return ErrorResponse.build(HttpStatus.BAD_REQUEST, "MALFORMED_REQUEST", "Invalid or malformed JSON request");
	}

	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<ErrorResponse> handleAccessDenied(AccessDeniedException ex) {
		log.warn("AccessDeniedException: {}", ex.getMessage());
		return ErrorResponse.build(HttpStatus.FORBIDDEN, "ACCESS_DENIED",
				"You do not have permission to access this resource");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
		log.error("Unexpected exception: {}", ex.getMessage(), ex);
		return ErrorResponse.build(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR");
	}
}
