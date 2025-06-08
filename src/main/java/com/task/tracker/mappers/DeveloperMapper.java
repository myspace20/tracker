package com.task.tracker.mappers;

import com.task.tracker.dto.DeveloperResponse;
import com.task.tracker.models.Developer;

public class DeveloperMapper {
    public static DeveloperResponse toDto(Developer developer) {
        return new DeveloperResponse(
                developer.getId(),
                developer.getName(),
                developer.getEmail()
        );
    }
}

