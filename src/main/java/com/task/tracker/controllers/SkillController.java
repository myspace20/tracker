package com.task.tracker.controllers;

import com.task.tracker.dto.SkillRequest;
import com.task.tracker.models.Skill;
import com.task.tracker.services.SkillService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = skillService.getAllSkills();
        return new ResponseEntity<>(skills, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(@Valid @RequestBody SkillRequest skill) {
        Skill newSkill = new Skill(skill.name());
        Skill savedSkill =  skillService.saveSkill(newSkill);
        return new ResponseEntity<>(savedSkill, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(@PathVariable Long id) {
        Skill skill = skillService.getSkillById(id);
        return new ResponseEntity<>(skill, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PutMapping("/{id}")
    public void updateSkill(@PathVariable Long id, @RequestBody Skill skill) {
        skillService.updateSkill(id,skill);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteSkill(@PathVariable Long id) {
        skillService.deleteSkillById(id);
    }
}
