package com.example.skillConnectBackend.service;
import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.SkillRepository;
import com.example.skillConnectBackend.repository.UserRepository;
import com.example.skillConnectBackend.service.SkillService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SkillServiceImpl  {

    @Autowired
    private SkillRepository skillRepository;

    @Autowired
    private UserRepository userRepository;

    public Skill addSkill(Skill skill) {
        return skillRepository.save(skill);
    }

 
    public List<Skill> getAllSkills() {
        return skillRepository.findAll();
    }


    public void removeSkill(Long skillId) {
        skillRepository.deleteById(skillId);
    }

   


    public void assignSkillToUser(Long userId, Long skillId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Skill> skill = skillRepository.findById(skillId);

        if (user.isPresent() && skill.isPresent()) {
            user.get().getSkills().add(skill.get());
            userRepository.save(user.get());
        } else {
            throw new IllegalArgumentException("User or Skill not found");
        }
    }

 
    public void removeSkillFromUser(Long userId, Long skillId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Skill> skill = skillRepository.findById(skillId);

        if (user.isPresent() && skill.isPresent()) {
            user.get().getSkills().remove(skill.get());
            userRepository.save(user.get());
        } else {
            throw new IllegalArgumentException("User or Skill not found");
        }
    }
}