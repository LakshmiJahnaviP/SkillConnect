package com.example.skillConnectBackend.repository;

import com.example.skillConnectBackend.model.Skill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Long> {
    // You can add custom query methods here if needed
}
