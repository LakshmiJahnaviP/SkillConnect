package com.example.skillConnectBackend.controller;

import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.service.SkillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import java.util.List;

@RestController
@RequestMapping("/skills") 
@CrossOrigin  
public class SkillController {

    @Autowired
    private SkillService skillService;

    
    @PostMapping("/add")
    public String add(@RequestBody Skill skill) {
        skillService.saveSkill(skill);  
        return "New skill is added";
    }

   
    @GetMapping("/getAll")
    public List<Skill> list() {
        return skillService.getAllSkills();  
    }
}