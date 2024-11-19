package com.example.skillConnectBackend.service;

import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.repository.SkillRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SkillService {

    public Skill saveSkill(Skill skill);
    public List<Skill> getAllSkills();
    
}
