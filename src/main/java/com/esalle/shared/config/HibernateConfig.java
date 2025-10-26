package com.esalle.shared.config;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.util.Properties;

/**
 * Hibernate configuration class
 * Uses ConfigurationManager for externalized properties
 */
public class HibernateConfig {
    
    /**
     * Get Hibernate configuration
     * @return Configuration object
     */
    public static Configuration getConfiguration() {
        Configuration configuration = new Configuration();
        ConfigurationManager config = ConfigurationManager.getInstance();
        
        Properties properties = new Properties();
        
        // Database connection properties from externalized config
        properties.setProperty(Environment.DRIVER, config.getDatabaseDriver());
        properties.setProperty(Environment.URL, config.getDatabaseUrl());
        properties.setProperty(Environment.USER, config.getDatabaseUsername());
        properties.setProperty(Environment.PASS, config.getDatabasePassword());
        properties.setProperty(Environment.DIALECT, config.getHibernateDialect());
        
        // Hibernate properties from externalized config
        properties.setProperty(Environment.SHOW_SQL, String.valueOf(config.isHibernateShowSql()));
        properties.setProperty(Environment.FORMAT_SQL, String.valueOf(config.isHibernateFormatSql()));
        properties.setProperty(Environment.HBM2DDL_AUTO, config.getHibernateHbm2ddlAuto());
        properties.setProperty(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        properties.setProperty(Environment.C3P0_MIN_SIZE, "5");
        properties.setProperty(Environment.C3P0_MAX_SIZE, "20");
        properties.setProperty(Environment.C3P0_ACQUIRE_INCREMENT, "1");
        properties.setProperty(Environment.C3P0_TIMEOUT, "300");
        properties.setProperty(Environment.C3P0_MAX_STATEMENTS, "50");
        
        configuration.setProperties(properties);
        
        // Add entity classes here
        // Module Auth & Users
        configuration.addAnnotatedClass(com.esalle.features.auth.domain.User.class);
        
        // Module Salle
        configuration.addAnnotatedClass(com.esalle.features.salle.domain.Salle.class);
        
        // Module Filière
        configuration.addAnnotatedClass(com.esalle.features.filiere.domain.Filiere.class);
        
        return configuration;
    }
}
