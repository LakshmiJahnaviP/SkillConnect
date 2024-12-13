package com.example.skillConnectBackend.service;

import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.repository.SkillRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SkillService {

     SkillRepository skillRepository;

    public List<Skill> findSkillsByName(String name) {
        return skillRepository.findByNameContainingIgnoreCase(name);
    }
    
    public List<Skill> findSkillsByNameContaining(String query) {
        return skillRepository.findByNameContainingIgnoreCase(query);
    }
    public List<Skill> findAll() {
        return skillRepository.findAll();
      }
    public Skill save(Skill skill) {
        return skillRepository.save(skill);
      }
      
      public Skill update(Long id, Skill skill) {
        Skill existingSkill = skillRepository.findById(id).orElseThrow();
        existingSkill.setName(skill.getName());
        return skillRepository.save(existingSkill);
      }
      
      public void delete(Long id) {
        skillRepository.deleteById(id);
      }
      

}
