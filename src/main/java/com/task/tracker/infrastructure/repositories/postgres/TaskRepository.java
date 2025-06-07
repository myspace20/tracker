package com.task.tracker.infrastructure.repositories.postgres;

import com.task.tracker.models.Project;
import com.task.tracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;



@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByDeveloperId(Long developerId);

    @Query("SELECT t FROM Task t WHERE t.dueDate < CURRENT_DATE")
    List<Task> findOverdueTasks();

    @Query("SELECT t.status AS status, COUNT(t) AS count FROM Task t GROUP BY t.status")
    List<TaskStatusCount> getTaskCountByStatus();
    record TaskStatusCount(String status, Long count) {}

}
