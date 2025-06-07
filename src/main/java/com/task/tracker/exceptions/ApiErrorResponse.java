package com.task.tracker.exceptions;

import java.time.LocalDateTime;

public record ApiErrorResponse(LocalDateTime timestamp,String errorMessage,int statusCode, String path) {
}
