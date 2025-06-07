package com.task.tracker.controllers;


import com.task.tracker.dto.DeveloperRequest;
import com.task.tracker.infrastructure.repositories.postgres.DeveloperRepository;
import com.task.tracker.models.Developer;
import com.task.tracker.services.DeveloperService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developers")
public class DeveloperController {

    private final DeveloperService developerService;

    public DeveloperController(DeveloperService developerService) {
        this.developerService = developerService;
    }

    @GetMapping
    public List<Developer> getAllDevelopers(
            @RequestParam(value = "pageNo", defaultValue = "0", required = false) int pageNo,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize
    ) {
        return developerService.getAllDevelopers(pageNo, pageSize);
    }

    @PostMapping
    public Developer createDeveloper(@RequestBody DeveloperRequest developer) {
        DeveloperRequest developerToCreate = new DeveloperRequest(developer.name(), developer.email());
        Developer newDeveloper = new Developer(developerToCreate.name(), developerToCreate.email());
        return developerService.saveDeveloper(newDeveloper);
    }

    @GetMapping("/{id}")
    public Developer getDeveloperById(@PathVariable long id) {
        return developerService.getDeveloperById(id);
    }

    @PutMapping("/{id}")
    public void updateDeveloper(@PathVariable Long id, @RequestBody Developer developer) {
        developerService.updateDeveloper(id, developer);
    }

    @GetMapping("/top_5")
    public List<DeveloperRepository.DeveloperTaskCount> getDeveloperTaskCounts() {
        return developerService.findTop5DevelopersWithTaskCount();
    }

    @DeleteMapping("/{id}")
    public void deleteDeveloper(@PathVariable long id) {
        developerService.deleteDeveloper(id);
    }

}
