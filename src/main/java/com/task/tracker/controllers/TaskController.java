package com.task.tracker.controllers;

import com.task.tracker.dto.TaskRequest;
import com.task.tracker.dto.TaskResponse;
import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import com.task.tracker.mappers.TaskMapper;
import com.task.tracker.models.Task;
import com.task.tracker.services.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/v1/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam (value = "sortBy", defaultValue = "status", required = false) String sortBy
            ) {
        List<Task> tasks  = taskService.getTasks(pageNo,pageSize,sortBy);
        List<TaskResponse> taskResponses = tasks.stream().map(TaskMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(taskResponses, HttpStatus.OK);
    }


    @GetMapping("/status/count")
    public ResponseEntity<List<TaskRepository.TaskStatusCount>> getTaskCountByStatus(){
        List<TaskRepository.TaskStatusCount> taskStatusCounts = taskService.getTaskCountByStatus();
        return new ResponseEntity<>(taskStatusCounts, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        return ResponseEntity.ok(TaskMapper.toDto(task));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<TaskResponse> createTask(@Valid @RequestBody TaskRequest task) {
        Task newTask = new Task(task.title(),task.description(), task.status(), task.dueDate());
        Task savedTask = taskService.saveTask(newTask);
        return ResponseEntity.status(HttpStatus.CREATED).body(TaskMapper.toDto(savedTask));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('DEVELOPER') and @IsAssignedDeveloperService.isAssignedDeveloper(#id, authentication.principal.id) or hasRole('ADMIN')")
    public void updateTask(@PathVariable Long id, @RequestBody Task task, Authentication authentication) {
        System.out.println(authentication.getPrincipal());
        taskService.updateTask(id, task);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }

    @GetMapping("/projects/{project_id}")
    public ResponseEntity<List<Task>> getTasksByProjectId(@PathVariable Long project_id) {
        List<Task> tasks = taskService.getTasksByProject(project_id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/developers/{developer_id}")
    public ResponseEntity<List<Task>> getTasksByDeveloperId(@PathVariable Long developer_id) {
        List<Task> tasks = taskService.getTasksByDeveloper(developer_id);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{task_id}/developers/{user_id}")
    public void assignTaskToDeveloper(@PathVariable Long user_id, @PathVariable Long task_id) {
        taskService.assignTaskToDeveloper(task_id, user_id);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{task_id}/projects/{project_id}")
    public void assignTaskToProject(@PathVariable Long task_id, @PathVariable Long project_id) {
        taskService.assignTaskToProject(task_id, project_id);
    }
}
