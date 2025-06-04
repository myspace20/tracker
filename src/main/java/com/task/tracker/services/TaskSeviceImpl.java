package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import com.task.tracker.models.Task;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSeviceImpl implements TaskService{

    private final TaskRepository taskRepository;

    public TaskSeviceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public List<Task> getTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Long id ,Task task) {
        Task taskToUpdate = this.getTaskById(id);
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setTitle(task.getTitle());
        return taskRepository.save(taskToUpdate);

    }

    @Override
    public void deleteTask(Long id) {
        this.getTaskById(id);
        taskRepository.deleteById(id);
    }
}
