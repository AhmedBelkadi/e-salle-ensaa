package com.esalle.filter;

import com.esalle.exception.ApplicationException;
import com.esalle.exception.BusinessException;
import com.esalle.exception.DataAccessException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Global Exception Handler Filter
 * Catches all exceptions and provides centralized error handling
 */
public class ExceptionHandlerFilter implements Filter {
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization if needed
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        // Don't handle exceptions for error pages to avoid infinite loops
        String requestURI = httpRequest.getRequestURI();
        if (requestURI != null && requestURI.contains("/error")) {
            chain.doFilter(request, response);
            return;
        }
        
        try {
            chain.doFilter(request, response);
        } catch (ServletException e) {
            // Check if this is a JSP compilation error
            Throwable rootCause = e.getRootCause();
            if (rootCause != null) {
                String rootCauseMsg = rootCause.getMessage();
                // JSP compilation errors often contain "org.apache.jasper" or mention JSP files
                if (rootCauseMsg != null && 
                    (rootCauseMsg.contains("jsp") || 
                     rootCauseMsg.contains("JasperException") ||
                     rootCause.getClass().getName().contains("jasper"))) {
                    // This is a JSP compilation/runtime error - log it but don't redirect
                    System.err.println("=== JSP Error Detected ===");
                    System.err.println("JSP Error: " + rootCause.getMessage());
                    System.err.println("JSP File causing error: " + extractJSPFile(rootCause));
                    rootCause.printStackTrace();
                    System.err.println("==========================");
                    // Still re-throw so Tomcat can handle it properly
                }
            }
            // Re-throw ServletException - let Tomcat handle it
            throw e;
        } catch (IOException e) {
            // Re-throw IOException
            throw e;
        } catch (RuntimeException e) {
            // Only catch RuntimeException and our custom exceptions
            if (e instanceof ApplicationException || 
                e instanceof BusinessException || 
                e instanceof DataAccessException) {
                handleException(e, httpRequest, httpResponse);
            } else {
                // For other runtime exceptions, log and handle
                handleException(e, httpRequest, httpResponse);
            }
        } catch (Exception e) {
            handleException(e, httpRequest, httpResponse);
        }
    }
    
    @Override
    public void destroy() {
        // Cleanup if needed
    }
    
    private void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // Log the exception (you can add proper logging here)
        System.err.println("Exception caught by global handler: " + exception.getMessage());
        exception.printStackTrace();
        
        // Determine error response based on exception type
        if (exception instanceof BusinessException) {
            handleBusinessException((BusinessException) exception, request, response);
        } else if (exception instanceof DataAccessException) {
            handleDataAccessException((DataAccessException) exception, request, response);
        } else if (exception instanceof ApplicationException) {
            handleApplicationException((ApplicationException) exception, request, response);
        } else {
            handleGenericException(exception, request, response);
        }
    }
    
    private void handleBusinessException(BusinessException exception, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        if (isAjaxRequest(request)) {
            sendJsonErrorResponse(response, "BUSINESS_ERROR", exception.getUserMessage(), 400);
        } else {
            // Redirect to appropriate page with error message
            String referer = request.getHeader("Referer");
            if (referer != null && !referer.isEmpty()) {
                response.sendRedirect(referer + "?error=business&message=" + 
                    java.net.URLEncoder.encode(exception.getUserMessage(), "UTF-8"));
            } else {
                response.sendRedirect(request.getContextPath() + "/error?type=business&message=" + 
                    java.net.URLEncoder.encode(exception.getUserMessage(), "UTF-8"));
            }
        }
    }
    
    private void handleDataAccessException(DataAccessException exception, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        if (isAjaxRequest(request)) {
            sendJsonErrorResponse(response, "DATA_ACCESS_ERROR", exception.getUserMessage(), 500);
        } else {
            response.sendRedirect(request.getContextPath() + "/error?type=data&message=" + 
                java.net.URLEncoder.encode(exception.getUserMessage(), "UTF-8"));
        }
    }
    
    private void handleApplicationException(ApplicationException exception, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        if (isAjaxRequest(request)) {
            sendJsonErrorResponse(response, exception.getErrorCode(), exception.getUserMessage(), 500);
        } else {
            response.sendRedirect(request.getContextPath() + "/error?type=application&message=" + 
                java.net.URLEncoder.encode(exception.getUserMessage(), "UTF-8"));
        }
    }
    
    private void handleGenericException(Exception exception, HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        
        // More detailed logging
        String exceptionClass = exception.getClass().getName();
        String exceptionMessage = exception.getMessage() != null ? exception.getMessage() : "Unknown error";
        
        System.err.println("=== Generic Exception Caught ===");
        System.err.println("Class: " + exceptionClass);
        System.err.println("Message: " + exceptionMessage);
        if (exception.getCause() != null) {
            System.err.println("Cause: " + exception.getCause().getClass().getName() + " - " + exception.getCause().getMessage());
        }
        exception.printStackTrace();
        System.err.println("================================");
        
        if (isAjaxRequest(request)) {
            sendJsonErrorResponse(response, "INTERNAL_ERROR", "Une erreur interne s'est produite", 500);
        } else {
            // Include exception class in message for debugging (in development only)
            String message = "Une erreur interne s'est produite";
            if (exceptionMessage != null && !exceptionMessage.isEmpty()) {
                message += " (" + exceptionClass + ")";
            }
            response.sendRedirect(request.getContextPath() + "/error?type=internal&message=" + 
                java.net.URLEncoder.encode(message, "UTF-8"));
        }
    }
    
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
    
    private void sendJsonErrorResponse(HttpServletResponse response, String errorCode, String message, int statusCode) 
            throws IOException {
        
        response.setStatus(statusCode);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        String jsonResponse = String.format(
            "{\"success\":false,\"errorCode\":\"%s\",\"message\":\"%s\"}", 
            errorCode, message
        );
        
        PrintWriter out = response.getWriter();
        out.print(jsonResponse);
        out.flush();
    }
    
    /**
     * Extract JSP file name from exception stack trace
     */
    private String extractJSPFile(Throwable t) {
        StackTraceElement[] stack = t.getStackTrace();
        for (StackTraceElement element : stack) {
            String fileName = element.getFileName();
            if (fileName != null && fileName.endsWith(".jsp")) {
                return fileName + " at line " + element.getLineNumber();
            }
        }
        // Check cause as well
        if (t.getCause() != null) {
            String causeResult = extractJSPFile(t.getCause());
            if (causeResult != null && !causeResult.isEmpty()) {
                return causeResult;
            }
        }
        return "Unknown JSP file";
    }
}

