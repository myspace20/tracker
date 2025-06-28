package com.task.tracker.services;

import com.task.tracker.events.TaskDuePublisher;
import com.task.tracker.exceptions.ResourceNotFound;
import com.task.tracker.infrastructure.repositories.postgres.TaskRepository;
import com.task.tracker.models.Project;
import com.task.tracker.models.Task;
import com.task.tracker.models.User;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskSeviceImpl implements TaskService{

    private final Counter taskCounter;
    private final MeterRegistry meterRegistry;

    private final TaskRepository taskRepository;
    private final UserService userService;
    private final ProjectService projectService;

    private final TaskDuePublisher eventPublisher;


    public TaskSeviceImpl(MeterRegistry meterRegistry, TaskRepository taskRepository, UserService userService, ProjectService projectService, TaskDuePublisher eventPublisher) {
        this.meterRegistry = meterRegistry;
        this.taskRepository = taskRepository;
        this.userService = userService;
        this.projectService = projectService;
        this.eventPublisher = eventPublisher;
        this.taskCounter = Counter.builder("total.tasks.created")
                .description("Number of taks processed")
                .tag("tasks","processed")
                .register(this.meterRegistry);
    }

    private void incrementTaskCounter() {
        taskCounter.increment();
    }

    private void incrementTaskCounter(int amount) {
        taskCounter.increment(amount);
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
    @Cacheable(value = "paginated_tasks", key = "{#pageNo,#pageSize,#sortBy}")
    public List<Task> getTasks(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortBy));
        incrementTaskCounter(pageable.getPageSize());
        return taskRepository.findAll(pageable).getContent();
    }

    @Override
    @Cacheable(value = "tasks", key = "#id")
    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Task not found"));
    }

    @Override
    @Cacheable(value = "project_tasks", key = "#projectId")
    public List<Task> getTasksByProject(Long projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    @Override
    @Cacheable(value = "developer_tasks", key = "#developerId")
    public List<Task> getTasksByDeveloper(Long developerId) {
        return taskRepository.findByUserId(developerId);
    }

    @Override
    @CacheEvict(value = {"tasks","paginated_tasks", "developer_tasks"}, allEntries = true)
    public Task saveTask(Task task) {
        incrementTaskCounter();
        return taskRepository.save(task);
    }

    @Override
    @CacheEvict(value = {"tasks","paginated_tasks"},key = "#taskId", allEntries = true)
    public Task assignTaskToDeveloper(Long taskId, Long userId) {
        Task task = getTaskById(taskId);
        User developer = userService.getUserById(userId);
        task.setUser(developer);
        incrementTaskCounter();
        return saveTask(task);
    }

    @Override
    @CacheEvict(value = {"tasks","paginated_tasks"}, key = "#taskId", allEntries = true)
    public Task assignTaskToProject(Long projectId, Long taskId) {

        Task task = getTaskById(taskId);
        Project project = projectService.getProjectById(projectId);
        task.setProject(project);
        return saveTask(task);
    }


    @Override
    @CacheEvict(value = {"tasks","paginated_tasks"}, key = "#id", allEntries = true)
    public Task updateTask(Long id, Task task) {
        Task taskToUpdate = getTaskById(id);
        taskToUpdate.setDescription(task.getDescription());
        taskToUpdate.setStatus(task.getStatus());
        taskToUpdate.setDueDate(task.getDueDate());
        taskToUpdate.setTitle(task.getTitle());
        incrementTaskCounter();
        return saveTask(taskToUpdate);
    }


    @Override
    @Transactional
    @CacheEvict(value = {"tasks","paginated_tasks"},  key = "#id", allEntries = true)
    public void deleteTask(Long id) {
        Task tasksToDelete = getTaskById(id);
        incrementTaskCounter();
        taskRepository.delete(tasksToDelete);
    }
}
