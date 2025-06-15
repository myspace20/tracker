package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import org.springframework.stereotype.Service;


@Service("IsAssignedDeveloperService")
public class IsAssignedDeveloperServiceImpl implements IsAssignedDeveloperService {

    private final TaskRepository taskRepository;

    public IsAssignedDeveloperServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }


    @Override
    public boolean isAssignedDeveloper(Long resource_id, Long user_id) {
        return taskRepository.findById(resource_id)
                .map(task -> task.getUser().getId().equals(user_id))
                .orElse(false);
    }
}
