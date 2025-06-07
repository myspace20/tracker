package com.task.tracker.dto;

import java.sql.Date;

public record DeveloperResponse(
        Long id,
        String developerName,
        String developerEmail
) {
}
