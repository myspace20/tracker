package com.task.tracker.controllers;

import com.task.tracker.models.AuditLog;
import com.task.tracker.services.AuditService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/v1/audit_logs")
public class AuditLogController {

    private final AuditService auditService;

    public AuditLogController(AuditService auditService) {
        this.auditService = auditService;
    }

    @GetMapping
    public List<AuditLog> getAllAuditLogs() {
        return auditService.getAuditLogs();
    }

    @GetMapping("/{actorName}")
    public AuditLog getAuditLogActorName(@PathVariable String actorName) {
        return auditService.findByActorName(actorName);
    }
}
