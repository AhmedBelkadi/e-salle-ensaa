package com.esalle.shared.exception;

/**
 * Base application exception
 * All custom exceptions should extend this class
 */
public class ApplicationException extends RuntimeException {
    
    private final String errorCode;
    private final String userMessage;
    
    public ApplicationException(String message) {
        super(message);
        this.errorCode = "APP_ERROR";
        this.userMessage = message;
    }
    
    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "APP_ERROR";
        this.userMessage = message;
    }
    
    public ApplicationException(String errorCode, String message, String userMessage) {
        super(message);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }
    
    public ApplicationException(String errorCode, String message, String userMessage, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userMessage = userMessage;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public String getUserMessage() {
        return userMessage;
    }
}
