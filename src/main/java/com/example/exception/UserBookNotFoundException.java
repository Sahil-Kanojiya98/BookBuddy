package com.example.exception;

public class UserBookNotFoundException extends RuntimeException {

    public UserBookNotFoundException(String message) {
        super(message);
    }

    public UserBookNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
