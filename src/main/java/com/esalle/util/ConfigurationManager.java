package com.esalle.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Configuration Manager for externalized properties
 * Singleton pattern for centralized configuration management
 */
public class ConfigurationManager {
    
    private static ConfigurationManager instance;
    private Properties properties;
    
    private ConfigurationManager() {
        loadProperties();
    }
    
    public static synchronized ConfigurationManager getInstance() {
        if (instance == null) {
            instance = new ConfigurationManager();
        }
        return instance;
    }
    
    private void loadProperties() {
        properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("Unable to find application.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Error loading configuration properties", e);
        }
    }
    
    public String getProperty(String key) {
        return properties.getProperty(key);
    }
    
    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }
    
    public int getIntProperty(String key, int defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    public boolean getBooleanProperty(String key, boolean defaultValue) {
        String value = properties.getProperty(key);
        if (value != null) {
            return Boolean.parseBoolean(value);
        }
        return defaultValue;
    }
    
    // Database configuration methods
    public String getDatabaseUrl() {
        return getProperty("db.url");
    }
    
    public String getDatabaseUsername() {
        return getProperty("db.username");
    }
    
    public String getDatabasePassword() {
        return getProperty("db.password");
    }
    
    public String getDatabaseDriver() {
        return getProperty("db.driver");
    }
    
    // Hibernate configuration methods
    public String getHibernateDialect() {
        return getProperty("hibernate.dialect");
    }
    
    public String getHibernateHbm2ddlAuto() {
        return getProperty("hibernate.hbm2ddl.auto");
    }
    
    public boolean isHibernateShowSql() {
        return getBooleanProperty("hibernate.show_sql", false);
    }
    
    public boolean isHibernateFormatSql() {
        return getBooleanProperty("hibernate.format_sql", false);
    }
    
    // Application configuration methods
    public String getAppName() {
        return getProperty("app.name", "E-Salle Application");
    }
    
    public String getAppVersion() {
        return getProperty("app.version", "1.0.0");
    }
    
    public String getAppEncoding() {
        return getProperty("app.encoding", "UTF-8");
    }
    
    // Pagination configuration methods
    public int getDefaultPageSize() {
        return getIntProperty("pagination.default.size", 10);
    }
    
    public int getMaxPageSize() {
        return getIntProperty("pagination.max.size", 100);
    }
    
    // File upload configuration methods
    public long getMaxUploadSize() {
        return getIntProperty("upload.max.size", 10485760); // 10MB default
    }
    
    public String getAllowedUploadTypes() {
        return getProperty("upload.allowed.types", "jpg,jpeg,png,pdf");
    }
}

