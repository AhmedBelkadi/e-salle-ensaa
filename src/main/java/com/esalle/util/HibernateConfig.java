package com.esalle.util;

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
        
        // Add entity classes with new package paths
        // Module Auth & Users (Dev1)
        configuration.addAnnotatedClass(com.esalle.entity.User.class);
        
        // Module Salle (Dev1)
        configuration.addAnnotatedClass(com.esalle.entity.Salle.class);
        
        // Module Filière (Dev1)
        configuration.addAnnotatedClass(com.esalle.entity.Filiere.class);
        
        // Module Réclamation (Dev1)
        configuration.addAnnotatedClass(com.esalle.entity.Reclamation.class);
        
        // Module Matière (Dev2)
        configuration.addAnnotatedClass(com.esalle.entity.Matiere.class);
        
        // Module Emploi du Temps (Dev2)
        configuration.addAnnotatedClass(com.esalle.entity.EmploiDuTemps.class);
        
        // Module Réservation (Dev2)
        configuration.addAnnotatedClass(com.esalle.entity.Reservation.class);
        
        // Audit & Workflow
        configuration.addAnnotatedClass(com.esalle.entity.AuditLog.class);
        
        return configuration;
    }
}

