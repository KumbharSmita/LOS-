package com.imperacred.BankLoanApplication.exception;

public class ResourceNotFoundException extends RuntimeException {

    // Constructor that accepts a message
    public ResourceNotFoundException(String message) {
        super(message);
    }

    // Constructor that accepts a message and a cause
    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
