package com.task.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DeveloperRequest(
        @NotBlank(message = "Developer name is required")
        @Size(min = 1, max = 255, message = "Developer name must be between 1 and 255 characters")
        String name,
        @NotBlank(message = "Developer email is required")
        @Size(min = 1, max = 255, message = "Developer email must be between 1 and 255 characters")
        String email
) {
}
