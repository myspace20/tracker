package com.task.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public record TaskRequest(
        @NotBlank(message = "Task title is required")
        @Size(min = 1, max = 255, message = "Task title must be between 1 and 255 characters")
        String title,
        @NotBlank(message = "Task description is required")
        @Size(min = 1, max = 255, message = "Task description must be between 1 and 255 characters")
        String description,
        @NotBlank(message = "Task status is required")
        @Size(min = 1, max = 255, message = "Task status must be between 1 and 255 characters")
        String status,
        @NotNull(message = "Task due date is required")
        Date dueDate
) {
}
