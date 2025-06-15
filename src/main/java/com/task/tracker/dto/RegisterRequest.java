package com.task.tracker.dto;

import com.task.tracker.models.Roles;
import jakarta.validation.constraints.*;

public record RegisterRequest(
        @NotBlank(message = "Username is required")
        @Size(max = 100,message = "Username cannot exceed 100 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(max = 100,message = "Password cannot exceed 100 characters")
        String password,

        @NotNull(message = "Role is required")
        Roles role
) {
}
