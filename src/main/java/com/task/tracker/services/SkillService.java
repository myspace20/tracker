package com.task.tracker.services;

import com.task.tracker.models.Skill;

import java.util.List;

public interface SkillService {
    List<Skill> getAllSkills();
    Skill getSkillById(Long id);
    Skill saveSkill(Skill skill);
    void deleteSkillById(Long id);
    void updateSkill(Long id,Skill skill);
}
