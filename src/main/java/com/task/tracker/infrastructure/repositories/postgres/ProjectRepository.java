package com.task.tracker.infrastructure.repositories.postgres;

import com.task.tracker.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}
