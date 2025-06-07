package com.task.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.sql.Date;

public record ProjectRequest(
        @NotBlank(message = "Project name is required")
        @Size(min = 1, max = 255, message = "Name must be between 1 and 255 characters")
        String name,
        @NotBlank(message = "Project description is required")
        @Size(min = 1, max = 1000, message = "Project description must be between 1 and 1000 characters")
        String description,
        @NotNull(message = "Project deadline is required")
        Date deadline
) {
}
