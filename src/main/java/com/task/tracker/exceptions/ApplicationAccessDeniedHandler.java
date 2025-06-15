package com.task.tracker.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.tracker.models.AuditLog;
import com.task.tracker.services.AuditService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class ApplicationAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    public ApplicationAccessDeniedHandler(ObjectMapper objectMapper, AuditService auditService) {
        this.objectMapper = objectMapper;
        this.auditService = auditService;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ApiErrorResponse apiErrorResponse = ApiError(request, accessDeniedException.getMessage(), auditService);
        response.setContentType("application/json");
        objectMapper.writeValue(response.getOutputStream(), apiErrorResponse);
    }

    static ApiErrorResponse ApiError(HttpServletRequest request, String message, AuditService auditService) {
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                message,
                HttpServletResponse.SC_UNAUTHORIZED,
                request.getRequestURI()
        );
        Map<String, Object> payload = Map.of(
                "path", request.getRequestURI(),
                "method", request.getMethod(),
                "ip", request.getRemoteAddr(),
                "error", message
        );

        AuditLog auditLog = new AuditLog("UNAUTHENTICATED_ACCESS", 0L, "UNKNOWN", "UNKNOWN", payload);
        auditService.createAuditLog(auditLog);
        return apiErrorResponse;
    }
}
