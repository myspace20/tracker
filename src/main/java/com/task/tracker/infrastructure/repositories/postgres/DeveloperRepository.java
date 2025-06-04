package com.task.tracker.infrastructure.repositories.postgres;

import org.springframework.data.jpa.repository.JpaRepository;
import com.task.tracker.models.Developer;

public interface DeveloperRepository extends JpaRepository<Developer, Long>{
}



