package com.task.tracker.services;

import com.task.tracker.events.TaskDuePublisher;
import com.task.tracker.exceptions.ResourceNotFound;
import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import com.task.tracker.models.Project;
import com.task.tracker.models.Task;
import com.task.tracker.models.User;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSeviceImpl implements TaskService{

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectService projectService;

    private final TaskDuePublisher eventPublisher;


    public TaskSeviceImpl(TaskRepository taskRepository, UserService userService, ProjectService projectService, TaskDuePublisher eventPublisher) {
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectService = projectService;
        this.eventPublisher = eventPublisher;
    }


    @Override
//    @Scheduled(fixedDelay = 120000)
    public void findOverdueTasks(){
        List<Task> overdueTasks = taskRepository.findOverdueTasks();
        for (Task task : overdueTasks) {
            eventPublisher.publish(task);
        }
    }

    public List<TaskRepository.TaskStatusCount> getTaskCountByStatus(){
        return taskRepository.getTaskCountByStatus();
    }


    @Override
    public List<Task> getTasks(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        return taskRepository.findAll(pageable).getContent();
    }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Task not found"));
    }

    @Override
    public List<Task> getTasksByProject(Long projectId){
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    public List<Task> getTasksByDeveloper(Long developerId){
        return taskRepository.findByUserId(developerId);
    }

    @Override
    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public void assignTaskToDeveloper(Long taskId, Long userId) {
        Task task = getTaskById(taskId);
        User developer = userService.getUserById(userId);
        task.setUser(developer);
        saveTask(task);
    }

    @Override
    public void assignTaskToProject(Long projectId, Long taskId) {
        Task task = getTaskById(taskId);
        Project project = projectService.getProjectById(projectId);
        task.setProject(project);
        saveTask(task);
    }


    @Override
    public void updateTask(Long id ,Task task) {
        Task taskToUpdate = getTaskById(id);
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setTitle(task.getTitle());
        saveTask(taskToUpdate);
    }

    @Override
    @Transactional
    public void deleteTask(Long id) {
        Task tasksToDelete = getTaskById(id);
        taskRepository.delete(tasksToDelete);
    }
}
