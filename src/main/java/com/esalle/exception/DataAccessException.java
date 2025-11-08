package com.esalle.exception;

/**
 * Data access exception
 * Thrown when database operations fail
 */
public class DataAccessException extends ApplicationException {
    
    public DataAccessException(String message) {
        super("DATA_ACCESS_ERROR", message, "Une erreur s'est produite lors de l'accès aux données");
    }
    
    public DataAccessException(String message, Throwable cause) {
        super("DATA_ACCESS_ERROR", message, "Une erreur s'est produite lors de l'accès aux données", cause);
    }
}

