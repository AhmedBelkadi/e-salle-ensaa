package com.esalle.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Error page servlet
 * Handles error display for different types of exceptions
 */
public class ErrorServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String errorType = request.getParameter("type");
        String errorMessage = request.getParameter("message");
        
        // Set error attributes for JSP
        request.setAttribute("errorType", errorType != null ? errorType : "unknown");
        request.setAttribute("errorMessage", errorMessage != null ? errorMessage : "Une erreur s'est produite");
        
        // Determine appropriate error page
        String errorPage = getErrorPage(errorType);
        
        request.getRequestDispatcher(errorPage).forward(request, response);
    }
    
    private String getErrorPage(String errorType) {
        if ("business".equals(errorType)) {
            return "/WEB-INF/views/error/business-error.jsp";
        } else if ("data".equals(errorType)) {
            return "/WEB-INF/views/error/data-error.jsp";
        } else if ("application".equals(errorType)) {
            return "/WEB-INF/views/error/application-error.jsp";
        } else if ("internal".equals(errorType)) {
            return "/WEB-INF/views/error/generic-error.jsp";
        } else {
            return "/WEB-INF/views/error/generic-error.jsp";
        }
    }
}

