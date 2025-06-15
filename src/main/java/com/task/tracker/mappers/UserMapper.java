package com.task.tracker.mappers;

import com.task.tracker.dto.UserResponse;
import com.task.tracker.models.User;

public class UserMapper {
    public static UserResponse toDto(User user) {
        return new UserResponse(
                user.getId(),
                user.getUserEmail()
        );
    }
}

