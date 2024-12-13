package com.example.skillConnectBackend.controller;

import com.example.skillConnectBackend.responses.ErrorResponse;
import com.example.skillConnectBackend.responses.SuccessResponse;
import com.example.skillConnectBackend.responses.UserProfileUpdateRequest;
import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.SkillRepository;
import com.example.skillConnectBackend.repository.UserRepository;
import com.example.skillConnectBackend.service.SkillService;
import com.example.skillConnectBackend.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import com.example.skillConnectBackend.controller.LoginRequest;

import java.security.Principal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
	private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    
    @Autowired
    private SkillRepository skillRepository;


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (user.getFirstName() == null || user.getFirstName().isEmpty()) {
                return ResponseEntity.badRequest().body("First name is required");
            }
            if (user.getEmail() == null || user.getEmail().isEmpty()) {
                return ResponseEntity.badRequest().body("Email is required");
            }
            if (user.getUsername() == null || user.getUsername().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required");
            }
            if (user.getPassword() == null || user.getPassword().isEmpty()) {
                return ResponseEntity.badRequest().body("Password is required");
            }

            // Encrypt password
            user.setPassword(passwordEncoder.encode(user.getPassword()));

            // Log before saving the user
            System.out.println("Saving user: " + user);

            // Save the user
            User savedUser = userRepository.save(user);

            // Log after saving
            System.out.println("User saved: " + savedUser);

            return ResponseEntity.ok(savedUser);
        } catch (Exception e) {
            // Log the exception for debugging purposes
            e.printStackTrace();
            
            // Return the error message as part of the response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Registration failed", e.getMessage()));
        }
    }

   
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        Logger logger = LoggerFactory.getLogger(UserController.class);

        // Log the incoming request
        logger.debug("Received login request: {}", loginRequest);

        if (loginRequest == null || loginRequest.getIdentifier() == null || loginRequest.getPassword() == null) {
            logger.error("Login request is missing required fields");
            return ResponseEntity.badRequest().body(new ErrorResponse("Invalid request", "Identifier and password are required"));
        }
        logger.info("Received login request for identifier: {}", loginRequest.getIdentifier());

        try {
            String identifier = loginRequest.getIdentifier();
            String password = loginRequest.getPassword();

            // Find user by email or user name
            Optional<User> user = userRepository.findByEmail(identifier)
                    .or(() -> userRepository.findByUsername(identifier));

            if (user.isEmpty()) {
                // Log error if user is not found
                logger.error("Login failed: User not found for identifier {}", identifier);
                return ResponseEntity.badRequest().body(new ErrorResponse("User not found", "No user found for the given identifier"));
            }

            // Check if the password matches the encoded one
            if (!passwordEncoder.matches(password, user.get().getPassword())) {
                // Log error if password does not match
                logger.error("Login failed: Incorrect password for identifier {}", identifier);
                return ResponseEntity.badRequest().body(new ErrorResponse("Incorrect password", "Password does not match"));
            }

            // Log successful login
            logger.info("Login successful for user: {}", user.get().getUsername());

            // Return a JSON response indicating success and the user's first name
            return ResponseEntity.ok(new SuccessResponse("Login successful", user.get(), user.get().getFirstName()));
        } catch (Exception e) {
            // Log unexpected errors
            logger.error("Unexpected error during login: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Unexpected error", e.getMessage()));
        }
    }
    

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        // Remove the JWT token cookie
        Cookie cookie = new Cookie("auth_token", null);
        cookie.setMaxAge(0);  // Expire the cookie immediately
        cookie.setHttpOnly(true); // Secure flag to prevent client-side access
        cookie.setPath("/");  // Set path to root so that it's removed from the entire domain
        response.addCookie(cookie);

        return ResponseEntity.ok("Logout successful");
    }
    
    


 // End point to get user profile details
   

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestParam Long userId) {
    	
        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return ResponseEntity.ok(user); // Includes skills in response
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("User not found", "No user found with the provided ID"));
        }
    }


    // End point to update profile excluding password
    @PutMapping("/profile")
    public ResponseEntity<?> editUserProfile(@RequestParam Long userId, @RequestBody User updatedUser) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Update user basic details
            user.setUsername(updatedUser.getUsername());
            user.setFirstName(updatedUser.getFirstName());
            user.setLastName(updatedUser.getLastName());
            user.setEmail(updatedUser.getEmail());

            // Get current skills and prepare incoming skills
            Set<Skill> currentSkills = user.getSkills();
            Set<Skill> incomingSkills = new HashSet<>();

            for (Skill skill : updatedUser.getSkills()) {
                Skill existingSkill = skillRepository.findById(skill.getId())
                        .orElseThrow(() -> new RuntimeException("Skill not found: " + skill.getId()));
                incomingSkills.add(existingSkill);
            }

            // Identify skills to remove
            Set<Skill> skillsToRemove = new HashSet<>(currentSkills);
            skillsToRemove.removeAll(incomingSkills); // Skills in currentSkills but not in incomingSkills

            // Remove only the missing skills
            currentSkills.removeAll(skillsToRemove);

            // Add new skills
            currentSkills.addAll(incomingSkills);

            // Save the updated skills
            user.setSkills(currentSkills);
            userRepository.save(user);

            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }



    // End point to change password
    @PutMapping("/change-password")
    public ResponseEntity<?> changePassword(@RequestParam Long userId, @RequestBody PasswordChangeRequest request) {
        // Check if the old password and new password confirmation match
        if (!request.getNewPassword().equals(request.getNewPasswordConfirm())) {
            // New password and confirmation do not match, return an error response
            ErrorResponse errorResponse = new ErrorResponse(
                "Passwords do not match", 
                "The new password and confirmation password must match"
            );
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            // Verify the old password
            if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
                // Update the password with the new one
                user.setPassword(passwordEncoder.encode(request.getNewPassword()));
                userRepository.save(user);

                // Create the success response
                SuccessResponse successResponse = new SuccessResponse(
                    "Password changed successfully", 
                    user, 
                    user.getFirstName()
                );

                return ResponseEntity.ok(successResponse); // Return success response
            } else {
                // Old password is incorrect, return an error response
                ErrorResponse errorResponse = new ErrorResponse(
                    "Old password is incorrect", 
                    "Please provide the correct old password"
                );
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
        }

        // User not found, return an error response
        ErrorResponse errorResponse = new ErrorResponse(
            "User not found", 
            "No user found with the provided ID"
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
    
   
 


}