package com.task.tracker.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.task.tracker.dto.TokenResponse;
import com.task.tracker.services.AuditService;
import com.task.tracker.util.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JWTUtil jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private final AuditService auditService;

    public OAuth2AuthenticationSuccessHandler(JWTUtil jwtTokenProvider, AuditService auditService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.auditService = auditService;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

        String email = oAuth2User.getAttribute("email");

        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        String accessToken = jwtTokenProvider.generateAccesToken(email, roles);
        String refreshToken = jwtTokenProvider.generateRefreshToken(email, roles);

        TokenResponse tokenResponse = new TokenResponse(accessToken, refreshToken);


        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);
        objectMapper.writeValue(response.getWriter(), Map.of("token", tokenResponse));
    }
}
