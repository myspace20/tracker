package com.task.tracker.infrastructure.repositories.mongodb;

import com.task.tracker.models.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}
