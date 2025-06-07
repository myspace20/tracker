package com.task.tracker.controllers;

import com.task.tracker.dto.TaskRequest;
import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import com.task.tracker.models.Project;
import com.task.tracker.models.Task;
import com.task.tracker.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<Task> getAllTasks(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "status", required = false) String sortBy
            ) {
        return taskService.getTasks(pageNo,pageSize,sortBy);
    }


    @GetMapping("/status/count")
    public List<TaskRepository.TaskStatusCount> getTaskCountByStatus(){
        return taskService.getTaskCountByStatus();
    }

    @GetMapping("/{id}")
    public Task getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public Task createTask(@Valid @RequestBody TaskRequest task) {
        Task newTask = new Task(task.title(),task.description(), task.status(), task.dueDate());
        return taskService.saveTask(newTask);
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id, @RequestBody Task task) {
        taskService.updateTask(id, task);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/projects/{project_id}")
    public List<Task> getTasksByProjectId(@PathVariable Long project_id) {
        return taskService.getTasksByProject(project_id);
    }

    @GetMapping("/developers/{developer_id}")
    public List<Task> getTasksByDeveloperId(@PathVariable Long developer_id) {
        return taskService.getTasksByDeveloper(developer_id);
    }

    @PutMapping("/{task_id}/developers/{user_id}")
    public void assignTaskToDeveloper(@PathVariable Long user_id, @PathVariable Long task_id) {
        taskService.assignTaskToDeveloper(task_id, user_id);
    }

    @PutMapping("/{task_id}/projects/{project_id}")
    public void assignTaskToProject(@PathVariable Long task_id, @PathVariable Long project_id) {
        taskService.assignTaskToProject(task_id, project_id);
    }
}
