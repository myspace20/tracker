package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.RoleRepository;
import com.task.tracker.infrastructure.repositories.postgres.UserRepository;
import com.task.tracker.models.Oauth2user;
import com.task.tracker.models.Role;
import com.task.tracker.models.Roles;
import com.task.tracker.models.User;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class CustomOAuth2UserServiceImpl extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public CustomOAuth2UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oauthUser = super.loadUser(userRequest);

        String email = oauthUser.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email not found in OAuth2 provider response");
        }

        Boolean emailVerified = oauthUser.getAttribute("email_verified");
        if (Boolean.FALSE.equals(emailVerified)) {
            throw new OAuth2AuthenticationException("Email not verified by provider.");
        }

        User user = userRepository.findByEmail(email)
                .orElseGet(() -> createNewUser(email));

        return new Oauth2user(user, oauthUser.getAttributes());
    }

    private User createNewUser(String email) {

        User newUser = new User();
        newUser.setEmail(email);

        Role role = roleRepository.findByName(Roles.ROLE_CONTRACTOR.name()).orElseThrow(()->
                new OAuth2AuthenticationException("Role not found"));

        newUser.getRoles().add(role);

        return userRepository.save(newUser);
    }
}
