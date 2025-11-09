package com.esalle.controller;

import com.esalle.util.HibernateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Health check servlet for monitoring application status
 * Used by Docker health checks and monitoring systems
 */
@WebServlet(name = "HealthServlet", urlPatterns = {"/health"})
public class HealthServlet extends HttpServlet {
    
    private static final Logger LOGGER = Logger.getLogger(HealthServlet.class.getName());
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", System.currentTimeMillis());
        health.put("service", "E-Salle ENSAA");
        
        // Check database connection
        boolean dbHealthy = checkDatabase();
        health.put("database", dbHealthy ? "UP" : "DOWN");
        
        // Check Hibernate SessionFactory
        boolean hibernateHealthy = HibernateUtil.isSessionFactoryAvailable();
        health.put("hibernate", hibernateHealthy ? "UP" : "DOWN");
        
        // Overall status
        if (!dbHealthy || !hibernateHealthy) {
            health.put("status", "DOWN");
            response.setStatus(503); // Service Unavailable
        } else {
            response.setStatus(200); // OK
        }
        
        // Write JSON response
        PrintWriter out = response.getWriter();
        try {
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(health);
            out.print(json);
        } catch (Exception e) {
            // Fallback to manual JSON if Jackson fails
            LOGGER.warning("Failed to serialize health check with Jackson: " + e.getMessage());
            out.print("{\"status\":\"" + health.get("status") + "\",");
            out.print("\"database\":\"" + health.get("database") + "\",");
            out.print("\"hibernate\":\"" + health.get("hibernate") + "\",");
            out.print("\"timestamp\":" + health.get("timestamp") + "}");
        }
        out.flush();
    }
    
    /**
     * Check database connectivity
     * @return true if database is accessible, false otherwise
     */
    private boolean checkDatabase() {
        try {
            if (!HibernateUtil.isSessionFactoryAvailable()) {
                return false;
            }
            
            try (Session session = HibernateUtil.getSessionFactory().openSession()) {
                Query<?> query = session.createQuery("SELECT 1");
                query.uniqueResult();
                return true;
            }
        } catch (Exception e) {
            LOGGER.warning("Database health check failed: " + e.getMessage());
            return false;
        }
    }
}