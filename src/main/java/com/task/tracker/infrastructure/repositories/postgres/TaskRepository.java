package com.task.tracker.infrastructure.repositories.postgres;

import com.task.tracker.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
