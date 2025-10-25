package com.esalle.shared.exception;

/**
 * Custom exception for persistence layer errors
 */
public class PersistenceException extends RuntimeException {
    
    public PersistenceException(String message) {
        super(message);
    }
    
    public PersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
}
