package com.task.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginRequest(
        @NotBlank(message = "Username is required")
        @Size(max = 100,message = "Username cannot exceed 100 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(max = 100,message = "Password cannot exceed 100 characters")
        String password
) {
}
