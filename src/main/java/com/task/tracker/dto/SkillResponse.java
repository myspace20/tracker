package com.task.tracker.dto;

import java.sql.Date;

public record SkillResponse(
        String name,
        Date createdAt,
        Date updatedAt
) {
}
