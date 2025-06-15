package com.task.tracker.dto;

public record TokenResponse(
        String refreshToken,
        String accessToken
) {
}
