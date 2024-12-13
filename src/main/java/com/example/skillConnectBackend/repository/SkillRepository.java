package com.example.skillConnectBackend.repository;

import com.example.skillConnectBackend.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SkillRepository extends JpaRepository<Skill, Long> {
    List<Skill> findByNameContainingIgnoreCase(String name);
    Optional<Skill> findByName(String name);
    
}
