package com.task.tracker.services;

import com.task.tracker.models.Developer;

import java.util.List;

public interface DeveloperService {
    List<Developer> getAllDevelopers();
    Developer getDeveloperById(Long id);
    Developer saveDeveloper(Developer developer);
    void deleteDeveloper(Long id);
    void updateDeveloper(Long id,Developer developer);
}
