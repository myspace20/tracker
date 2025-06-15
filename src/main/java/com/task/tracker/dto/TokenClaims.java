package com.task.tracker.dto;

import com.task.tracker.models.Roles;

import java.util.List;

public record TokenClaims(
        String email,
        List<String> roles
) {
}
