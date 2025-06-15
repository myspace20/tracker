package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.UserRepository;
import com.task.tracker.models.SecurityUser;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;



@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
       return userRepository.findByEmail(email).map(
               (SecurityUser::new)
       ).orElseThrow(() -> new UsernameNotFoundException(email));
    }

}
