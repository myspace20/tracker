package com.task.tracker.dto;


public record DeveloperResponse(
        Long id,
        String developerName,
        String developerEmail
) {
}
