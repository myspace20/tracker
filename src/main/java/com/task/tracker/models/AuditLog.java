package com.task.tracker.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.Instant;
import java.util.Map;

@Document(collection = "audit_logs")
public class AuditLog {

    @Id
    private String id;

    private String actionType;

    private String entityType;

    private Long entityId;

    private Instant timestamp;

    private String actorName;

    @Field("payload")
    private Object payload;

    public AuditLog() {}

    public AuditLog(String actionType, Long entityId, String entityType,
                    String actorName, Object payload) {
        this.actionType = actionType;
        this.entityType = entityType;
        this.entityId = entityId;
        this.actorName = actorName;
        this.payload = payload;
        this.timestamp = Instant.now();
    }


    public String getId() {
        return id;
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

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public Object getPayload() {
        return payload;
    }

    public void setPayload(Map<String, Object> payload) {
        this.payload = payload;
    }
}
