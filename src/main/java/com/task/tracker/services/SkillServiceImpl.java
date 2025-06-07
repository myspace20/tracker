package com.task.tracker.services;

import com.task.tracker.infrastructure.repositories.postgres.SkillRepository;
import com.task.tracker.models.Skill;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillServiceImpl implements SkillService {
    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }

    @Override
    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public Skill getSkillById(Long id) {
        return skillRepository.findById(id).orElse(null);
    }

    @Override
    public void deleteSkillById(Long id) {
        Skill skill = getSkillById(id);
        skillRepository.delete(skill);
    }

    @Override
    public void updateSkill(Long id, Skill skill) {
        Skill updatedSkill = this.getSkillById(id);
        updatedSkill.setSkillName(skill.getSkillName());
        skillRepository.save(updatedSkill);
    }
}


