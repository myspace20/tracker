package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.mongodb.AuditLogRepository;
import com.task.tracker.models.AuditLog;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl implements AuditService {

    private final AuditLogRepository auditLogRepository;


    public AuditServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    public AuditLog createAuditLog(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    @Override
    public AuditLog findByActorName(String actorName) {
        return auditLogRepository.findByActorName(actorName);
    }

    @Override
    public List<AuditLog> getAuditLogs(){
        return auditLogRepository.findAll();
    }
}
