package com.example.skillConnectBackend.service;
import com.example.skillConnectBackend.model.Notification;
import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.model.Skill;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.PostRepository;
import com.example.skillConnectBackend.repository.SkillRepository;
import com.example.skillConnectBackend.repository.UserRepository;
import com.example.skillConnectBackend.responses.PostResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SkillRepository skillRepository;
    
    @Autowired
    private NotificationService notificationService;

    public List<Post> filterPostsBySkillIds(List<Long> skillIds) {
        return postRepository.findBySkillIds(skillIds);
    }
    public List<Map<String, Object>> getAllPostsWithUsernames() {
        List<Post> posts = postRepository.findAll();
        return posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("id", post.getId());
            postMap.put("content", post.getContent());
            postMap.put("timestamp", post.getTimestamp());
            postMap.put("username", post.getUser().getUsername());
            postMap.put("firstName", post.getUser().getFirstName());
            postMap.put("taggedUsers", post.getTaggedUsers().stream()
                .map(taggedUser -> Map.of(
                    "id", taggedUser.getId(),
                    "name", taggedUser.getFirstName() + " " + taggedUser.getLastName()
                ))
                .collect(Collectors.toList())); // Include tagged users
            postMap.put("skills", post.getSkills().stream()
                .map(skill -> Map.of("id", skill.getId(), "name", skill.getName()))
                .collect(Collectors.toList())); // Include skills
            return postMap;
        }).collect(Collectors.toList());
    }


    public List<Map<String, Object>> getAllPostsWithTaggedUsers() {
        List<Post> posts = postRepository.findAllPostsWithTaggedUsers();

        return posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("id", post.getId());
            postMap.put("content", post.getContent());
            postMap.put("timestamp", post.getTimestamp());
            postMap.put("username", post.getUser().getUsername());
            postMap.put("skills", post.getSkills().stream()
                .map(skill -> Map.of("id", skill.getId(), "name", skill.getName()))
                .collect(Collectors.toList()));

            postMap.put("taggedUsers", post.getTaggedUsers().stream()
                .map(taggedUser -> Map.of("id", taggedUser.getId(), "name", taggedUser.getFirstName() + " " + taggedUser.getLastName()))
                .collect(Collectors.toList()));

            return postMap;
        }).collect(Collectors.toList());
    }

    

    public Post createPost(Long userId, String content, List<Long> skillIds, List<Long> taggedUserIds) {
        // Fetch the creator of the post
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        // Create the post
        Post post = new Post();
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());
        post.setUser(user);

        // Fetch skills
        List<Skill> skills = skillRepository.findAllById(skillIds);
        post.setSkills(new HashSet<>(skills));

        // Fetch tagged users
        Set<User> taggedUsers = new HashSet<>(userRepository.findAllById(taggedUserIds));
        post.setTaggedUsers(taggedUsers);

        // Save the post
        Post savedPost = postRepository.save(post);

        // Create notifications for tagged users
        for (User taggedUser : taggedUsers) {
            String message = "You were tagged in a post by " + user.getFirstName() + "!";
            Notification notification = new Notification();
            notification.setUser(taggedUser);
            notification.setMessage(message);
            notification.setRead(false);
            notification.setTimestamp(LocalDateTime.now());

            notificationService.createNotification(taggedUser, "You were tagged in a post by " + user.getUsername());
        }

        return savedPost;
    }


    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    

    
}