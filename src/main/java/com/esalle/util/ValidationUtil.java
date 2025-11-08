package com.esalle.util;

import com.esalle.exception.BusinessException;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

/**
 * Validation utility for Bean Validation
 * Provides centralized validation logic
 */
public class ValidationUtil {
    
    private static Validator validator;
    
    static {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    
    /**
     * Validate an object using Bean Validation annotations
     * @param object Object to validate
     * @throws BusinessException if validation fails
     */
    public static void validate(Object object) throws BusinessException {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        
        if (!violations.isEmpty()) {
            StringBuilder errorMessage = new StringBuilder();
            for (ConstraintViolation<Object> violation : violations) {
                if (errorMessage.length() > 0) {
                    errorMessage.append("; ");
                }
                errorMessage.append(violation.getMessage());
            }
            throw new BusinessException("Validation failed: " + errorMessage.toString());
        }
    }
    
    /**
     * Validate an object and return validation errors as a formatted string
     * @param object Object to validate
     * @return Formatted validation errors or empty string if valid
     */
    public static String getValidationErrors(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        
        if (violations.isEmpty()) {
            return "";
        }
        
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<Object> violation : violations) {
            if (errorMessage.length() > 0) {
                errorMessage.append("<br>");
            }
            errorMessage.append("• ").append(violation.getMessage());
        }
        return errorMessage.toString();
    }
    
    /**
     * Check if an object is valid
     * @param object Object to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValid(Object object) {
        Set<ConstraintViolation<Object>> violations = validator.validate(object);
        return violations.isEmpty();
    }
}

