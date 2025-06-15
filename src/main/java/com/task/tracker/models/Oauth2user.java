package com.task.tracker.models;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Map;
import java.util.stream.Collectors;

public class Oauth2user extends DefaultOAuth2User {
    private final User user;

    public Oauth2user(User user, Map<String, Object> attributes) {
        super(
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList()),
                attributes,
                "email"
        );
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}

