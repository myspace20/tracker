package com.task.tracker.controllers;


import com.task.tracker.dto.LoginRequest;
import com.task.tracker.dto.RefreshTokenRequest;
import com.task.tracker.dto.RegisterRequest;
import com.task.tracker.dto.TokenResponse;
import com.task.tracker.models.User;
import com.task.tracker.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public TokenResponse login(@Valid  @RequestBody LoginRequest user) {
        return authService.login(user);
    }

    @PostMapping("/register")
    public User register(@Valid  @RequestBody RegisterRequest user) {
        return authService.register(user);
    }

    @PostMapping("/refresh")
    public TokenResponse generateRefreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
       return authService.generateRefreshToken(refreshTokenRequest.refreshToken());
    }


}
