package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.DeveloperRepository;
import com.task.tracker.models.Developer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperServiceImpl(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Override
    public List<Developer> getAllDevelopers() {
        return developerRepository.findAll();
    }

    @Override
    public  Developer saveDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }

    @Override
    public  Developer getDeveloperById(Long id) {
        return developerRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteDeveloper(Long id) {
        developerRepository.deleteById(id);
    }

    @Override
    public void updateDeveloper(Long id, Developer developer) {
        Developer developerToBeUpdated = this.getDeveloperById(id);
        developerToBeUpdated.setName(developer.getName());
        developerToBeUpdated.setEmail(developer.getEmail());
        developerRepository.save(developerToBeUpdated);
    }

}
