package com.task.tracker.mappers;

import com.task.tracker.dto.ProjectResponse;
import com.task.tracker.models.Project;

public class ProjectMapper {
    public static ProjectResponse toDto(Project project) {
        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getDeadline()
        );
    }
}
