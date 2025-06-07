package com.task.tracker.dto;

import java.sql.Date;

public record TaskResponse(
        Long taskId,
        String title,
        String description,
        String status,
        Date dueDate
) {
}
