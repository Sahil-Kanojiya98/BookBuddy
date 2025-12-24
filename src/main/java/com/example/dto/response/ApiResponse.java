package com.example.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private final LocalDateTime timestamp;
    private final int status;
    private final String message;
    private final T data;

    public ApiResponse(int status, String message, T data) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(int status, String message) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = message;
        this.data = null;
    }

    public ApiResponse(int status) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.message = null;
        this.data = null;
    }

    public static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, String message, T data) {
        ApiResponse<T> response = new ApiResponse<>(status.value(), message, data);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, String message) {
        ApiResponse<T> response = new ApiResponse<>(status.value(), message, null);
        return ResponseEntity.status(status).body(response);
    }

    public static <T> ResponseEntity<ApiResponse<T>> build(HttpStatus status, T data) {
        ApiResponse<T> response = new ApiResponse<>(status.value(), null, data);
        return ResponseEntity.status(status).body(response);
    }

    public static ResponseEntity<ApiResponse<Void>> build(HttpStatus status) {
        ApiResponse<Void> response = new ApiResponse<>(status.value(), null, null);
        return ResponseEntity.status(status).body(response);
    }
}
