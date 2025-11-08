package com.esalle.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 * Hibernate utility class for managing SessionFactory
 */
public class HibernateUtil {
    
    private static SessionFactory sessionFactory;
    private static volatile boolean initializationAttempted = false;
    private static volatile RuntimeException initializationException = null;
    
    /**
     * Get the SessionFactory instance
     * @return SessionFactory or null if initialization failed
     * @throws RuntimeException if SessionFactory initialization failed
     */
    public static SessionFactory getSessionFactory() {
        if (sessionFactory != null) {
            return sessionFactory;
        }
        
        // Lazy initialization to avoid issues during class loading
        synchronized (HibernateUtil.class) {
            if (initializationAttempted) {
                if (initializationException != null) {
                    throw initializationException;
                }
                return sessionFactory;
            }
            
            initializationAttempted = true;
            try {
                Configuration configuration = HibernateConfig.getConfiguration();
                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties())
                        .build();
                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
                return sessionFactory;
            } catch (Exception e) {
                initializationException = new RuntimeException("Failed to create SessionFactory", e);
                throw initializationException;
            }
        }
    }
    
    /**
     * Close the SessionFactory
     */
    public static void closeSessionFactory() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}

