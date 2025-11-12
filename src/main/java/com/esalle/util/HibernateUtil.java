package com.esalle.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Logger;

/**
 * Hibernate utility class for managing SessionFactory
 * Singleton pattern with thread-safe lazy initialization
 */
public class HibernateUtil {
    
    private static final Logger LOGGER = Logger.getLogger(HibernateUtil.class.getName());
    
    private static SessionFactory sessionFactory;
    private static volatile boolean initializationAttempted = false;
    private static volatile RuntimeException initializationException = null;
    
    /**
     * Private constructor to prevent instantiation
     */
    private HibernateUtil() {
        // Private constructor
    }
    
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
                LOGGER.info("🔧 Initializing Hibernate SessionFactory...");
                
                // Create registry
                StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                        .configure() // Load hibernate.cfg.xml from classpath
                        .build();
                
                try {
                    // Create MetadataSources
                    MetadataSources sources = new MetadataSources(registry);
                    
                    // Build Metadata
                    Metadata metadata = sources.getMetadataBuilder().build();
                    
                    // Build SessionFactory
                    sessionFactory = metadata.getSessionFactoryBuilder().build();
                    
                    LOGGER.info("✅ Hibernate SessionFactory initialized successfully");
                    
                    return sessionFactory;
                    
                } catch (Exception e) {
                    // Destroy registry if SessionFactory creation fails
                    StandardServiceRegistryBuilder.destroy(registry);
                    throw e;
                }
                
            } catch (Exception e) {
                LOGGER.severe("❌ Failed to create SessionFactory: " + e.getMessage());
                e.printStackTrace();
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
            try {
                LOGGER.info("🔌 Closing Hibernate SessionFactory...");
                sessionFactory.close();
                LOGGER.info("✅ Hibernate SessionFactory closed successfully");
            } catch (Exception e) {
                LOGGER.warning("⚠️ Error closing SessionFactory: " + e.getMessage());
            }
        }
    }
    
    /**
     * Check if SessionFactory is initialized and open
     * @return true if SessionFactory is available and open
     */
    public static boolean isSessionFactoryAvailable() {
        return sessionFactory != null && !sessionFactory.isClosed();
    }
}
