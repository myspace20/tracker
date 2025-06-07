package com.task.tracker.controllers;

import com.task.tracker.dto.SkillRequest;
import com.task.tracker.models.Skill;
import com.task.tracker.services.SkillService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @GetMapping
    public List<Skill> getAllSkills() {
        return skillService.getAllSkills();
    }

    @PostMapping
    public Skill createSkill(@Valid @RequestBody SkillRequest skill) {
        Skill newSkill = new Skill(skill.name());
        return  skillService.saveSkill(newSkill);
    }

    @GetMapping("/{id}")
    public Skill getSkillById(@PathVariable Long id) {
        return skillService.getSkillById(id);
    }

    @PutMapping("/{id}")
    public void updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        skillService.updateSkill(id,skill);
    }

    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.deleteSkillById(id);
    }
}
