package com.task.tracker.services;

import com.task.tracker.models.AuditLog;

import java.util.List;


public interface AuditService {
    List<AuditLog> getAuditLogs();
    AuditLog createAuditLog(AuditLog auditLog);
}
