package com.task.tracker.services;

import com.task.tracker.models.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAllProjects(int page, int size);
    Project getProjectById(Long id);
    Project createProject(Project project);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
    List<Project> findProjectsWithoutTasks();
}
