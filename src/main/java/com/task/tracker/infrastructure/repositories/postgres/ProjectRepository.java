package com.task.tracker.infrastructure.repositories.postgres;

import com.task.tracker.models.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    @Query("SELECT p FROM Project p WHERE p.tasks IS EMPTY")
    List<Project> findProjectsWithoutTasks();

}
