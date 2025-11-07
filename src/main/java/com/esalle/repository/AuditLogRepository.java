package com.esalle.repository;

import com.esalle.entity.AuditLog;
import java.util.List;

public interface AuditLogRepository extends BaseRepository<AuditLog, Long> {
    AuditLog save(AuditLog log);
    List<AuditLog> findAll();
    List<AuditLog> findByUserId(Long userId);
    List<AuditLog> findByEntity(String entityType, Long entityId);
}

