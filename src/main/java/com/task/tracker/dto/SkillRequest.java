package com.task.tracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SkillRequest(
        @NotBlank(message = "Skill name is required")
        @Size(min = 1, max = 255, message = "Skill name must be between 1 and 255 characters")
        String name
) {

}
