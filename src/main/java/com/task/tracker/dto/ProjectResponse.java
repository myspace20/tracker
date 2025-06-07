package com.task.tracker.dto;

import java.sql.Date;

public record ProjectResponse(
        Long id,
        String name,
        String description,
        Date deadline
) {
}
