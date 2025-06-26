package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import com.task.tracker.models.Project;
import com.task.tracker.models.Task;

import java.util.List;
import java.util.Optional;

public interface TaskService {
    List<Task> getTasks(int pageNo, int pageSize, String sortBy);
    Task getTaskById(Long id);
    Task saveTask(Task task);
    void updateTask(Long id, Task task);
    void deleteTask(Long id);
    void assignTaskToDeveloper(Long taskId, Long userId);
    void assignTaskToProject(Long taskId, Long projectId);
    List<Task> getTasksByProject(Long projectId);
    List<Task> getTasksByDeveloper(Long developerId);
    void findOverdueTasks();
    List<TaskRepository.TaskStatusCount> getTaskCountByStatus();
}
