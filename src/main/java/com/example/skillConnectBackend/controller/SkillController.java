package com.example.skillConnectBackend.controller;

import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.SkillRepository;
import com.example.skillConnectBackend.service.SkillService;
import com.example.skillConnectBackend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class SkillController {
	
	@Autowired
    private SkillService skillService;

    @Autowired
    private UserService userService;
    
    @Autowired
    private SkillRepository skillRepository;
    
    @GetMapping("/skills")
    public ResponseEntity<List<Skill>> getAllSkills() {
        List<Skill> skills = skillRepository.findAll();
        return ResponseEntity.ok(skills);
    }
    @GetMapping("/suggestions")
    public ResponseEntity<?> getSkillSuggestions(@RequestParam String query) {
        List<Skill> suggestions = skillService.findSkillsByNameContaining(query);
        return ResponseEntity.ok(suggestions);
    }

    
	
	  @GetMapping("/users/{userId}/skills")
	    public ResponseEntity<Optional<User>> getUserWithSkills(@PathVariable Long userId){
	        return new ResponseEntity<>(userService.findByIdWithSkills(userId), HttpStatus.OK);
	    }

    @PostMapping("/users/{userId}/skills")
    public Skill addSkillToUser(@PathVariable Long userId, @RequestBody Long skillId) {
        return userService.addSkillToUser(userId, skillId);
    }

    @DeleteMapping("/users/{userId}/skills/{skillId}")
    public void removeSkillFromUser(@PathVariable Long userId, @PathVariable Long skillId) {
        userService.removeSkillFromUser(userId, skillId);
    }
    
    @DeleteMapping("/skills/{id}")
    public void deleteSkill(@PathVariable Long id) {
      skillService.delete(id);
    }
    
    @PostMapping("/skills")
    public ResponseEntity<?> addSkill(@RequestBody Skill skill) {
        if (skill.getName() == null || skill.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Skill name cannot be empty");
        }

        // Check if the skill already exists
        Optional<Skill> existingSkill = skillRepository.findByName(skill.getName());
        if (existingSkill.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Skill already exists");
        }

        Skill newSkill = skillRepository.save(skill);
        return ResponseEntity.status(HttpStatus.CREATED).body(newSkill);
    }
    
    
}