package com.esalle.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * Filter for logging HTTP requests
 */
public class RequestLoggingFilter implements Filter {
    
    private static final Logger logger = Logger.getLogger(RequestLoggingFilter.class.getName());
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("RequestLoggingFilter initialized");
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        
        long startTime = System.currentTimeMillis();
        
        // Log request
        logger.info(String.format("Request: %s %s from %s", 
            httpRequest.getMethod(), 
            httpRequest.getRequestURI(), 
            httpRequest.getRemoteAddr()));
        
        try {
            chain.doFilter(request, response);
        } finally {
            long duration = System.currentTimeMillis() - startTime;
            logger.info(String.format("Response: %d %s (%dms)", 
                httpResponse.getStatus(), 
                httpRequest.getRequestURI(), 
                duration));
        }
    }
    
    @Override
    public void destroy() {
        logger.info("RequestLoggingFilter destroyed");
    }
}

