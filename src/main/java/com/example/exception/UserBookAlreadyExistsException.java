package com.example.exception;

public class UserBookAlreadyExistsException extends RuntimeException {

	public UserBookAlreadyExistsException(String message) {
		super(message);
	}

	public UserBookAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}
