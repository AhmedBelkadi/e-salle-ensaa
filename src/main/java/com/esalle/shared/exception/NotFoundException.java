package com.esalle.shared.exception;

/**
 * Exception levée quand une ressource n'est pas trouvée
 */
public class NotFoundException extends ApplicationException {
    
    public NotFoundException(String message) {
        super("NOT_FOUND", message, message);
    }
    
    public NotFoundException(String message, String userMessage) {
        super("NOT_FOUND", message, userMessage);
    }
    
    public NotFoundException(String message, Throwable cause) {
        super("NOT_FOUND", message, message, cause);
    }
}

