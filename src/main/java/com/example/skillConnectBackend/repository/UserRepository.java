package com.example.skillConnectBackend.repository;



import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.skillConnectBackend.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    Optional<User> findById(Long id);
    @Query("SELECT u FROM User u WHERE u.email = :identifier OR u.username = :identifier")
    User findByEmailOrUsername(@Param("identifier") String identifier);
    @Query("SELECT u FROM User u JOIN FETCH u.skills WHERE u.id = :userId")
    Optional<User> findByIdWithSkills(@Param("userId") Long userId);
    
    @Query("SELECT u FROM User u JOIN u.skills s WHERE s.id IN :skillIds")
    List<User> findUsersBySkillIds(@Param("skillIds") List<Long> skillIds);
}