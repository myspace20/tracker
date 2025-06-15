package com.task.tracker.filters;

import com.task.tracker.exceptions.ApplicationAuthenticationEntryPoint;
import com.task.tracker.exceptions.InvalidTokenException;
import com.task.tracker.exceptions.InsufficientAuthenticationException;
import com.task.tracker.services.UserDetailsServiceImpl;
import com.task.tracker.util.JWTUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;


@Component
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;
    private final ApplicationAuthenticationEntryPoint authenticationEntryPoint;

    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsService, ApplicationAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                username = jwtUtil.getUsernameFromToken(token).email();
            } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException | InvalidTokenException e) {
                InsufficientAuthenticationException insufficientAuthenticationException = new InsufficientAuthenticationException(e.getMessage());
                insufficientAuthenticationException.initCause(e);
                authenticationEntryPoint.commence(request, response,
                        new AuthenticationException(insufficientAuthenticationException.getMessage(), insufficientAuthenticationException) {
                        }
                );
                return;
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            try {
                if (jwtUtil.validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (ExpiredJwtException | MalformedJwtException | IllegalArgumentException | InvalidTokenException e) {
                InsufficientAuthenticationException insufficientAuthenticationException = new InsufficientAuthenticationException(e.getMessage());
                insufficientAuthenticationException.initCause(e);
                authenticationEntryPoint.commence(request, response,
                        new AuthenticationException(insufficientAuthenticationException.getMessage(), insufficientAuthenticationException) {
                        }
                );
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

}
