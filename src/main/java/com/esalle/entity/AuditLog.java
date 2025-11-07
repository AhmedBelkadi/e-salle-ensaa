package com.esalle.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Entité pour journaliser toutes les actions importantes selon le workflow
 * Permet de tracer l'historique complet des actions
 */
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "user_id")
    private Long userId;
    
    @Column(name = "user_nom", length = 200)
    private String userNom;
    
    @Column(name = "action_type", nullable = false, length = 50)
    private String actionType; // ADD, UPDATE, DELETE, APPROVE, REFUSE, etc.
    
    @Column(name = "entity_type", nullable = false, length = 50)
    private String entityType; // SALLE, FILIERE, RESERVATION, RECLAMATION, etc.
    
    @Column(name = "entity_id")
    private Long entityId;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "old_value", columnDefinition = "TEXT")
    private String oldValue; // Valeur avant modification (JSON ou texte)
    
    @Column(name = "new_value", columnDefinition = "TEXT")
    private String newValue; // Valeur après modification (JSON ou texte)
    
    @Column(name = "ip_address", length = 50)
    private String ipAddress;
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    // Constructors
    public AuditLog() {
        this.createdAt = LocalDateTime.now();
    }
    
    public AuditLog(Long userId, String userNom, String actionType, String entityType, Long entityId, String description) {
        this();
        this.userId = userId;
        this.userNom = userNom;
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.description = description;
    }
    
    // Lifecycle callbacks
    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getUserId() {
        return userId;
    }
    
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    
    public String getUserNom() {
        return userNom;
    }
    
    public void setUserNom(String userNom) {
        this.userNom = userNom;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public String getEntityType() {
        return entityType;
    }
    
    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }
    
    public Long getEntityId() {
        return entityId;
    }
    
    public void setEntityId(Long entityId) {
        this.entityId = entityId;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getOldValue() {
        return oldValue;
    }
    
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    
    public String getNewValue() {
        return newValue;
    }
    
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    
    public String getIpAddress() {
        return ipAddress;
    }
    
    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

