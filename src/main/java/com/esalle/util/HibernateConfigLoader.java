package com.esalle.util;

import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

import java.util.logging.Logger;

/**
 * Hibernate configuration loader that supports environment variables
 * Overrides hibernate.cfg.xml values with environment variables when available
 */
public class HibernateConfigLoader {
    
    private static final Logger LOGGER = Logger.getLogger(HibernateConfigLoader.class.getName());
    
    /**
     * Build SessionFactory with environment variable support
     * @return SessionFactory instance
     */
    public static SessionFactory buildSessionFactory() {
        LOGGER.info("🔧 Building Hibernate SessionFactory with environment variable support...");
        
        // Create registry builder
        StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder()
                .configure(); // Load hibernate.cfg.xml from classpath
        
        // Override with environment variables if available
        String dbUrl = System.getenv("DB_URL");
        String dbUser = System.getenv("DB_USERNAME");
        String dbPass = System.getenv("DB_PASSWORD");
        String ddlAuto = System.getenv("HIBERNATE_DDL_AUTO");
        
        if (dbUrl != null && !dbUrl.isEmpty()) {
            LOGGER.info("📝 Using DB_URL from environment: " + dbUrl.replaceAll("://.*@", "://***:***@"));
            registryBuilder.applySetting("hibernate.connection.url", dbUrl);
        }
        
        if (dbUser != null && !dbUser.isEmpty()) {
            LOGGER.info("📝 Using DB_USERNAME from environment");
            registryBuilder.applySetting("hibernate.connection.username", dbUser);
        }
        
        if (dbPass != null && !dbPass.isEmpty()) {
            LOGGER.info("📝 Using DB_PASSWORD from environment");
            registryBuilder.applySetting("hibernate.connection.password", dbPass);
        }
        
        if (ddlAuto != null && !ddlAuto.isEmpty()) {
            LOGGER.info("📝 Using HIBERNATE_DDL_AUTO from environment: " + ddlAuto);
            registryBuilder.applySetting("hibernate.hbm2ddl.auto", ddlAuto);
        } else {
            // Default to validate for production safety
            String defaultDdl = System.getProperty("hibernate.ddl.auto", "validate");
            LOGGER.info("📝 Using default HIBERNATE_DDL_AUTO: " + defaultDdl);
            registryBuilder.applySetting("hibernate.hbm2ddl.auto", defaultDdl);
        }
        
        // Build registry
        StandardServiceRegistry registry = registryBuilder.build();
        
        try {
            // Create MetadataSources
            MetadataSources sources = new MetadataSources(registry);
            
            // Build Metadata
            Metadata metadata = sources.getMetadataBuilder().build();
            
            // Build SessionFactory
            SessionFactory sessionFactory = metadata.getSessionFactoryBuilder().build();
            
            LOGGER.info("✅ Hibernate SessionFactory built successfully with environment variables");
            
            return sessionFactory;
            
        } catch (Exception e) {
            LOGGER.severe("❌ Failed to build SessionFactory: " + e.getMessage());
            e.printStackTrace();
            // Destroy registry if SessionFactory creation fails
            StandardServiceRegistryBuilder.destroy(registry);
            throw new RuntimeException("Failed to build SessionFactory", e);
        }
    }
}

