package com.task.tracker.services;

import com.task.tracker.dto.LoginRequest;
import com.task.tracker.dto.RegisterRequest;
import com.task.tracker.dto.TokenResponse;
import com.task.tracker.exceptions.InvalidTokenException;
import com.task.tracker.models.User;


public interface AuthService {

    User register(RegisterRequest registerRequest);
    TokenResponse login(LoginRequest loginRequest);
    TokenResponse generateRefreshToken(String refreshToken) throws InvalidTokenException;
}
