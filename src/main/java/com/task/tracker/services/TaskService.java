package com.task.tracker.services;

import com.task.tracker.models.Task;

import java.util.List;

public interface TaskService {
    List<Task> getTasks();
    Task getTaskById(Long id);
    Task saveTask(Task task);
    Task updateTask(Long id, Task task);
    void deleteTask(Long id);
}
