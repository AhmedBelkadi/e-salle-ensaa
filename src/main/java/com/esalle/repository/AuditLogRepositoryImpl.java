package com.esalle.repository;

import com.esalle.entity.AuditLog;
import org.hibernate.Session;

import java.util.List;

public class AuditLogRepositoryImpl extends BaseRepositoryImpl<AuditLog, Long> implements AuditLogRepository {
    
    public AuditLogRepositoryImpl() {
        super(AuditLog.class);
    }
    
    @Override
    public AuditLog save(AuditLog log) {
        Session session = sessionFactory.openSession();
        org.hibernate.Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            if (log.getId() == null) {
                session.persist(log);
            } else {
                log = (AuditLog) session.merge(log);
            }
            transaction.commit();
            return log;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Error saving audit log", e);
        } finally {
            session.close();
        }
    }
    
    // findById est déjà implémenté par BaseRepositoryImpl, pas besoin de le redéfinir
    
    @Override
    public List<AuditLog> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM AuditLog ORDER BY createdAt DESC", AuditLog.class)
                    .getResultList();
        }
    }
    
    @Override
    public List<AuditLog> findByUserId(Long userId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM AuditLog WHERE userId = :userId ORDER BY createdAt DESC", 
                    AuditLog.class)
                    .setParameter("userId", userId)
                    .getResultList();
        }
    }
    
    @Override
    public List<AuditLog> findByEntity(String entityType, Long entityId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery(
                    "FROM AuditLog WHERE entityType = :entityType AND entityId = :entityId ORDER BY createdAt DESC", 
                    AuditLog.class)
                    .setParameter("entityType", entityType)
                    .setParameter("entityId", entityId)
                    .getResultList();
        }
    }
}

