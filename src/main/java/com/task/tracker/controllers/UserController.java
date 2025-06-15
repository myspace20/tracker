package com.task.tracker.controllers;


import com.task.tracker.dto.UserResponse;
import com.task.tracker.infrastructure.repositories.postgres.UserRepository;
import com.task.tracker.mappers.UserMapper;
import com.task.tracker.models.User;
import com.task.tracker.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUserById(@PathVariable long id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(UserMapper.toDto(user));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable Long id, @RequestBody User user) {
        userService.updateUser(id, user);
    }

    @GetMapping("/top_5")
    public ResponseEntity<List<UserRepository.DeveloperTaskCount>> getDeveloperTaskCounts() {
        List<UserRepository.DeveloperTaskCount> developerTaskCounts = userService.findTop5DevelopersWithTaskCount();
        return ResponseEntity.ok(developerTaskCounts);
    }

    @GetMapping("/me")
    public String returnCurrentUser(Principal principal) {
        return principal.getName();
    }


}
