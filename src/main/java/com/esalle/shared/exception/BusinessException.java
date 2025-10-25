package com.esalle.shared.exception;

/**
 * Business logic exception
 * Thrown when business rules are violated
 */
public class BusinessException extends ApplicationException {
    
    public BusinessException(String message) {
        super("BUSINESS_ERROR", message, message);
    }
    
    public BusinessException(String message, String userMessage) {
        super("BUSINESS_ERROR", message, userMessage);
    }
    
    public BusinessException(String message, Throwable cause) {
        super("BUSINESS_ERROR", message, message, cause);
    }
}
