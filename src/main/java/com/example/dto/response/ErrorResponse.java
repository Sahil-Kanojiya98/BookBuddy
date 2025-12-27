package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private final LocalDateTime timestamp;
	private final int status;
	private final String error;
	private final String message;
	private final Map<String, String> fieldErrors;

	public ErrorResponse(int status, String error, String message, Map<String, String> fieldErrors) {
		this.timestamp = LocalDateTime.now();
		this.status = status;
		this.error = error;
		this.message = message;
		this.fieldErrors = fieldErrors;
	}

	public static ResponseEntity<ErrorResponse> build(HttpStatus status, String error) {
		ErrorResponse errorResponse = new ErrorResponse(status.value(), error, null, null);
		return ResponseEntity.status(status).body(errorResponse);
	}

	public static ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message) {
		ErrorResponse errorResponse = new ErrorResponse(status.value(), error, message, null);
		return ResponseEntity.status(status).body(errorResponse);
	}

	public static ResponseEntity<ErrorResponse> build(HttpStatus status, String error, String message,
			Map<String, String> fieldErrors) {
		ErrorResponse errorResponse = new ErrorResponse(status.value(), error, message, fieldErrors);
		return ResponseEntity.status(status).body(errorResponse);
	}
}
