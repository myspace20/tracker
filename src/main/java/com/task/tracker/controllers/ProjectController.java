package com.task.tracker.controllers;

import com.task.tracker.dto.ProjectRequest;
import com.task.tracker.dto.ProjectResponse;
import com.task.tracker.mappers.ProjectMapper;
import com.task.tracker.models.AuditLog;
import com.task.tracker.models.Project;
import com.task.tracker.services.AuditService;
import com.task.tracker.services.ProjectService;
import com.task.tracker.util.ObjectToPayload;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AuditService auditService;


    public ProjectController(ProjectService projectService, AuditService auditService) {
        this.projectService = projectService;
        this.auditService = auditService;
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ){
        List<Project> projects = projectService.getAllProjects(pageNo,pageSize);
        List<ProjectResponse> projectResponses = projects.stream()
                .map(ProjectMapper::toDto).collect(Collectors.toList());
        return new ResponseEntity<>(projectResponses, HttpStatus.OK);
    }

    @GetMapping("/tasks/zero")
    public ResponseEntity<List<Project>> findProjectsWithoutTasks(){
        List<Project> projects = projectService.findProjectsWithoutTasks();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }


    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{id}/summary")
    public ResponseEntity<ProjectResponse> getProjectSummary(@PathVariable Long id){
        Project project =  projectService.getProjectById(id);
        return ResponseEntity.ok( ProjectMapper.toDto(project));
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public ResponseEntity<ProjectResponse> createProject(@Valid @RequestBody ProjectRequest project){
        Project newProject = new Project(project.name(),project.description(),project.deadline());
        Project savedProject = projectService.createProject(newProject);
        Map<String, Object> payload = ObjectToPayload.createPayload("project",project);
        AuditLog auditLog = new AuditLog("create project",  savedProject.getId(),Project.class.getName(),"Roger",payload);
        auditService.createAuditLog(auditLog);
        return ResponseEntity.status(HttpStatus.CREATED).body(ProjectMapper.toDto(savedProject));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public void updateProject(@PathVariable Long id, @RequestBody Project project){
        Map<String, Object> payload = ObjectToPayload.createPayload("project",project);
        AuditLog auditLog = new AuditLog("update project", id,Project.class.getName(),"Roger",payload);
        auditService.createAuditLog(auditLog);
        projectService.updateProject(id, project);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id){
        projectService.deleteProject(id);
    }


}
