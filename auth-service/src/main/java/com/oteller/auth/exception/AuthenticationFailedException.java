package com.oteller.auth.exception;

public class AuthenticationFailedException extends RuntimeException {
    
    public AuthenticationFailedException(String message) {
        super(message);
    }
} 