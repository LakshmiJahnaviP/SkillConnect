package com.example.skillConnectBackend.service;
import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.repository.SkillRepository;
import com.example.skillConnectBackend.service.SkillService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl implements SkillService {

    @Autowired
    private SkillRepository skillRepository;

    @Override
    public Skill saveSkill(Skill skill) {
        return skillRepository.save(skill);
    }

    @Override
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }
}