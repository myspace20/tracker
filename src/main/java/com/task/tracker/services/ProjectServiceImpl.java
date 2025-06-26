package com.task.tracker.services;

import com.task.tracker.exceptions.ResourceNotFound;
import com.task.tracker.infrastructure.repositories.postgres.ProjectRepository;
import com.task.tracker.models.Project;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService{

    private final ProjectRepository projectRepository;

    public ProjectServiceImpl(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Override
    @Cacheable(value = "projects", key = "#page")
    public List<Project> getAllProjects(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return projectRepository.findAll(pageable).getContent();
    }

    @Override
    @Cacheable(value = "projects")
    public List<Project> findProjectsWithoutTasks(){
        return projectRepository.findProjectsWithoutTasks();
    }


    @Override
    @Cacheable(key = "#id", value = "projects")
    public Project getProjectById(Long id) {
        return projectRepository.findById(id).orElseThrow(() -> new ResourceNotFound("Project not found"));
    }

    @Override
    @CacheEvict(value={"projects"}, allEntries = true)
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }

    @Override
    @Transactional
    @CacheEvict(value={"projects"},  allEntries = true)
    public void deleteProject(Long id) {
        Project project = getProjectById(id);
        projectRepository.delete(project);
    }

    @Override
    @CacheEvict(value={"projects"},allEntries = true, key = "#id")
    public Project updateProject(Long id, Project project) {
        Project updatedProject = getProjectById(id);
        updatedProject.setName(project.getName());
        updatedProject.setName(project.getName());
        updatedProject.setDeadline(project.getDeadline());
        return projectRepository.save(updatedProject);
    }
}
