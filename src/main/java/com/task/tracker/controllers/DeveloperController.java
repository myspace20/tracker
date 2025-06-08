package com.task.tracker.controllers;


import com.task.tracker.dto.DeveloperRequest;
import com.task.tracker.dto.DeveloperResponse;
import com.task.tracker.infrastructure.repositories.postgres.DeveloperRepository;
import com.task.tracker.mappers.DeveloperMapper;
import com.task.tracker.models.Developer;
import com.task.tracker.services.DeveloperService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping
    public ResponseEntity<List<DeveloperResponse>> getAllDevelopers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        List<DeveloperResponse> developers = developerService.getAllDevelopers(pageNo, pageSize).stream()
                .map(DeveloperMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(developers);
    }

    @PostMapping
    public ResponseEntity<DeveloperResponse> createDeveloper(@Valid @RequestBody DeveloperRequest developer) {
        DeveloperRequest developerToCreate = new DeveloperRequest(developer.name(), developer.email());
        Developer newDeveloper = new Developer(developerToCreate.name(), developerToCreate.email());
        Developer newdeveloper = developerService.saveDeveloper(newDeveloper);
        return ResponseEntity.status(HttpStatus.CREATED).body(DeveloperMapper.toDto(newdeveloper));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeveloperResponse> getDeveloperById(@PathVariable long id) {
        Developer developer = developerService.getDeveloperById(id);
        return ResponseEntity.ok(DeveloperMapper.toDto(developer));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable Long id, @RequestBody Developer developer) {
        developerService.updateDeveloper(id, developer);
    }

    @GetMapping("/top_5")
    public ResponseEntity<List<DeveloperRepository.DeveloperTaskCount>> getDeveloperTaskCounts() {
        List<DeveloperRepository.DeveloperTaskCount> developerTaskCounts = developerService.findTop5DevelopersWithTaskCount();
        return ResponseEntity.ok(developerTaskCounts);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable long id) {
        developerService.deleteDeveloper(id);
    }

}
