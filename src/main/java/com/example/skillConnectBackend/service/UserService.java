package com.example.skillConnectBackend.service;

import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.UserRepository;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
    
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }
    
    // Save user to database
    public void saveUser(User user) {
        // First, check if the user already exists in the database
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("User with this email already exists");
        }

        // Save the user to the database
        userRepository.save(user);
    }
    
    public User authenticate(String identifier, String password) {
        User user = userRepository.findByEmailOrUsername(identifier);

        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null; // Invalid credentials
    }
    
    public User getUserProfile(Long userId) {
        // Assuming you have a repository with a method to find a user by ID
        Optional<User> user = userRepository.findById(userId);
        return user.orElse(null);  // Return the user or null if not found
    }

}