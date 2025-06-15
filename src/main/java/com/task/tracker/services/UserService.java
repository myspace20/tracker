package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.UserRepository;
import com.task.tracker.models.User;
import jakarta.transaction.Transactional;

import java.util.List;

public interface UserService {
    List<User> getAllUsers(int page, int size);
    User getUserById(Long id);
    List<UserRepository.DeveloperTaskCount> findTop5DevelopersWithTaskCount();
    @Transactional
    void deleteUser(Long id);
    void updateUser(Long id, User user);
}
