package com.example.exception;

import org.springframework.security.core.AuthenticationException;

public class AppAuthenticationException extends AuthenticationException {

    public AppAuthenticationException(String msg) {
        super(msg);
    }

    public AppAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
