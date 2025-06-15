package com.task.tracker.services;

import com.task.tracker.exceptions.ResourceNotFound;
import com.task.tracker.infrastructure.repositories.postgres.UserRepository;
import com.task.tracker.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public List<User> getAllUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return userRepository.findAll(pageable).getContent();
    }


    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Developer not found"));
    }

    @Override
    public List<UserRepository.DeveloperTaskCount> findTop5DevelopersWithTaskCount(){
        return userRepository.findTop5DevelopersWithTaskCount();
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    public void updateUser(Long id, User user) {
        User userToBeUpdated = getUserById(id);
        userToBeUpdated.setEmail(user.getUserEmail());
        userRepository.save(userToBeUpdated);
    }

}
