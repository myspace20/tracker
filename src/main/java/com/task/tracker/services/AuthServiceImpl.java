package com.task.tracker.services;

import com.task.tracker.dto.LoginRequest;
import com.task.tracker.dto.RegisterRequest;
import com.task.tracker.dto.TokenClaims;
import com.task.tracker.dto.TokenResponse;
import com.task.tracker.exceptions.ResourceNotFound;
import com.task.tracker.infrastructure.repositories.postgres.RoleRepository;
import com.task.tracker.infrastructure.repositories.postgres.UserRepository;
import com.task.tracker.models.Role;
import com.task.tracker.models.User;
import com.task.tracker.util.JWTUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RoleRepository roleRepository;

    public AuthServiceImpl(UserRepository userRepository, AuthenticationManager authenticationManager, JWTUtil jwtUtil, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.roleRepository = roleRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public User register(RegisterRequest registerRequest) {
        if (userRepository.findByEmail(registerRequest.email()).isPresent()) {
            throw new RuntimeException("Username is already taken");
        }
        String hashedPassword = passwordEncoder.encode(registerRequest.password());
        Role role = roleRepository.findByName(registerRequest.role().name()).orElseThrow(()->
                new ResourceNotFound("Role not found"));
        User user = new User(registerRequest.email(), hashedPassword);
        user.getRoles().add(role);
        return userRepository.save(user);
    }

    @Override
    public TokenResponse login(LoginRequest loginRequest){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        String accessToken = jwtUtil.generateAccesToken(authentication.getName(),roles);
        String refreshToken = jwtUtil.generateRefreshToken(authentication.getName(),roles);

        return  new TokenResponse(
                refreshToken,
                accessToken
        );
    }

    @Override
    public TokenResponse generateRefreshToken(String refreshToken) {
        boolean isValidRefreshToken = jwtUtil.validateToken(refreshToken);
        if (!isValidRefreshToken) {
            throw new RuntimeException("Invalid access token");
        }
        TokenClaims claims = jwtUtil.getUsernameFromToken(refreshToken);
        String newAccessToken = jwtUtil.generateAccesToken(claims.email(),claims.roles());
        String newRefreshToken = jwtUtil.generateRefreshToken(claims.email(),claims.roles());
        return new TokenResponse(newAccessToken,newRefreshToken);
    }


}
