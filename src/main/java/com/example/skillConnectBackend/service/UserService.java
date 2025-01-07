package com.example.skillConnectBackend.service;

import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.SkillRepository;
import com.example.skillConnectBackend.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SkillService skillService;
    
    @Autowired
    private SkillRepository skillRepository;
    
    
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    
    public void saveUser(User user) {
       
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        
        userRepository.save(user);
    }
    
    public User authenticate(String identifier, String password) {
        User user = userRepository.findByEmailOrUsername(identifier);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null; 
    }
    
    public User getUserProfile(Long userId) {
       
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);  
    }
    public Skill addSkillToUser(Long userId, Long skillId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new RuntimeException("Skill not found"));
        user.getSkills().add(skill);
        userRepository.save(user);
        return skill;
    }

    public void removeSkillFromUser(Long userId, Long skillId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Skill skill = skillRepository.findById(skillId).orElseThrow(() -> new RuntimeException("Skill not found"));
        user.getSkills().remove(skill);
        userRepository.save(user);
    }

    public Optional<User> findByIdWithSkills(Long userId){
        return userRepository.findById(userId);
    }
    
    public List<User> getUsersBySkillIds(List<Long> skillIds) {
        return userRepository.findUsersBySkillIds(skillIds);
    }
}