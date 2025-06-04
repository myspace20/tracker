package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.ProjectRepository;
import com.task.tracker.models.Project;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    @Override
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).get();
    }

    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    public void deleteProject(Long id) {
        projectRepository.deleteById(id);
    }

    @Override
    public void updateProject(Long id, Project project) {
        Project updatedProject = projectRepository.findById(id).get();
        updatedProject.setName(project.getName());
        updatedProject.setName(project.getName());
        updatedProject.setDeadline(project.getDeadline());
        projectRepository.save(updatedProject);
    }
}
