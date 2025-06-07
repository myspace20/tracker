package com.task.tracker.controllers;

import com.task.tracker.dto.ProjectRequest;
import com.task.tracker.models.Project;
import com.task.tracker.services.ProjectService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;


    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping
    public List<Project> getProjects(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){
        return projectService.getAllProjects(pageNo,pageSize);
    }

    @GetMapping("/tasks/zero")
    public List<Project> findProjectsWithoutTasks(){
        return projectService.findProjectsWithoutTasks();
    }


    @GetMapping("/{id}")
    public Project getProject(@PathVariable Long id){
        return projectService.getProjectById(id);
    }

    @PostMapping
    public Project createProject(@RequestBody ProjectRequest project){
        Project newProject = new Project(project.name(),project.description(),project.deadline());
        return projectService.createProject(newProject);
    }

    @PutMapping("/{id}")
    public void updateProject(@PathVariable Long id, @RequestBody Project project){
        projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }


}
