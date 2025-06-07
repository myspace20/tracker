package com.task.tracker.controllers;

import com.task.tracker.dto.ProjectRequest;
import com.task.tracker.models.AuditLog;
import com.task.tracker.models.Project;
import com.task.tracker.services.AuditService;
import com.task.tracker.services.ProjectService;
import com.task.tracker.util.ObjectToPayload;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AuditService auditService;


    public ProjectController(ProjectService projectService, AuditService auditService) {
        this.projectService = projectService;
        this.auditService = auditService;
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
    public Project createProject(@Valid @RequestBody ProjectRequest project){
        Project newProject = new Project(project.name(),project.description(),project.deadline());
        Project savedProject = projectService.createProject(newProject);
        Map<String, Object> payload = ObjectToPayload.createPayload("project",project);
        AuditLog auditLog = new AuditLog("create project",  savedProject.getId(),Project.class.getName(),"Roger",payload);
        auditService.createAuditLog(auditLog);
        return savedProject;
    }

    @PutMapping("/{id}")
    public void updateProject(@PathVariable Long id, @RequestBody Project project){
        Map<String, Object> payload = ObjectToPayload.createPayload("project",project);
        AuditLog auditLog = new AuditLog("update project", id,Project.class.getName(),"Roger",payload);
        auditService.createAuditLog(auditLog);
        projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }


}
