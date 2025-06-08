package com.task.tracker.mappers;

import com.task.tracker.dto.TaskResponse;
import com.task.tracker.models.Task;

public class TaskMapper {
    public static TaskResponse toDto(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getDueDate()
        );
    }
}
