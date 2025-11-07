package com.esalle.service;

import com.esalle.entity.AuditLog;
import com.esalle.entity.User;
import com.esalle.repository.AuditLogRepository;
import com.esalle.repository.AuditLogRepositoryImpl;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Service pour la journalisation des actions selon le workflow
 */
public class AuditService {
    
    private final AuditLogRepository auditLogRepository;
    
    public AuditService() {
        this.auditLogRepository = new AuditLogRepositoryImpl();
    }
    
    /**
     * Enregistre une action dans le journal d'audit
     */
    public void logAction(User user, String actionType, String entityType, Long entityId, 
                         String description, HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setUserId(user != null ? user.getId() : null);
        log.setUserNom(user != null ? (user.getPrenom() + " " + user.getNom()) : "SYSTEM");
        log.setActionType(actionType);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDescription(description);
        
        if (request != null) {
            String ipAddress = getClientIpAddress(request);
            log.setIpAddress(ipAddress);
        }
        
        auditLogRepository.save(log);
    }
    
    /**
     * Enregistre une modification avec ancienne et nouvelle valeur
     */
    public void logModification(User user, String entityType, Long entityId, 
                               String oldValue, String newValue, String description,
                               HttpServletRequest request) {
        AuditLog log = new AuditLog();
        log.setUserId(user != null ? user.getId() : null);
        log.setUserNom(user != null ? (user.getPrenom() + " " + user.getNom()) : "SYSTEM");
        log.setActionType("UPDATE");
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setDescription(description);
        
        if (request != null) {
            log.setIpAddress(getClientIpAddress(request));
        }
        
        auditLogRepository.save(log);
    }
    
    /**
     * Récupère l'adresse IP du client
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
    
    /**
     * Récupère les logs pour un utilisateur
     */
    public List<AuditLog> getLogsByUser(Long userId) {
        return auditLogRepository.findByUserId(userId);
    }
    
    /**
     * Récupère les logs pour une entité
     */
    public List<AuditLog> getLogsByEntity(String entityType, Long entityId) {
        return auditLogRepository.findByEntity(entityType, entityId);
    }
    
    /**
     * Récupère tous les logs (admin seulement)
     */
    public List<AuditLog> getAllLogs() {
        return auditLogRepository.findAll();
    }
}

