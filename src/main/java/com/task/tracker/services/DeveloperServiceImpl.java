package com.task.tracker.services;

import com.task.tracker.exceptions.ResourceNotFound;
import com.task.tracker.infrastructure.repositories.postgres.DeveloperRepository;
import com.task.tracker.models.Developer;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DeveloperServiceImpl implements DeveloperService {

    private final DeveloperRepository developerRepository;

    public DeveloperServiceImpl(DeveloperRepository developerRepository) {
        this.developerRepository = developerRepository;
    }

    @Override
    public List<Developer> getAllDevelopers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return developerRepository.findAll(pageable).getContent();
    }

    @Override
    public  Developer saveDeveloper(Developer developer) {
        return developerRepository.save(developer);
    }

    @Override
    public  Developer getDeveloperById(Long id) {
        return developerRepository.findById(id).orElseThrow(()-> new ResourceNotFound("Developer not found"));
    }

    @Override
    public List<DeveloperRepository.DeveloperTaskCount> findTop5DevelopersWithTaskCount(){
        return developerRepository.findTop5DevelopersWithTaskCount();
    }

    @Override
    @Transactional
    public void deleteDeveloper(Long id) {
        Developer developer = getDeveloperById(id);
        developerRepository.delete(developer);
    }

    @Override
    public void updateDeveloper(Long id, Developer developer) {
        Developer developerToBeUpdated = getDeveloperById(id);
        developerToBeUpdated.setName(developer.getName());
        developerToBeUpdated.setEmail(developer.getEmail());
        developerRepository.save(developerToBeUpdated);
    }

}
