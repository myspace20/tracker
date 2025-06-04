package com.task.tracker.infrastructure.repositories.postgres;

import com.task.tracker.models.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillRepository extends JpaRepository<Skill, Long> {
}
