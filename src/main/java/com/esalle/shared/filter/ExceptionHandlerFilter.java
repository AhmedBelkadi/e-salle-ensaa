package com.esalle.shared.filter;

import com.esalle.shared.exception.ApplicationException;
import com.esalle.shared.exception.BusinessException;
import com.esalle.shared.exception.DataAccessException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        
        try {
            chain.doFilter(request, response);
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
        
        if (isAjaxRequest(request)) {
            sendJsonErrorResponse(response, "INTERNAL_ERROR", "Une erreur interne s'est produite", 500);
        } else {
            response.sendRedirect(request.getContextPath() + "/error?type=internal&message=" + 
                java.net.URLEncoder.encode("Une erreur interne s'est produite", "UTF-8"));
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
}
