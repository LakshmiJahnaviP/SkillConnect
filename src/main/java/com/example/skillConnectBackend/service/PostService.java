package com.example.skillConnectBackend.service;
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
            postMap.put("username", post.getUser().getUsername()); // Include username
            postMap.put("skills", post.getSkills().stream()
                .map(skill -> Map.of("id", skill.getId(), "name", skill.getName()))
                .collect(Collectors.toList())); // Include skills
            return postMap;
        }).collect(Collectors.toList());
    }
    	
    

    public Post createPost(Long userId, String content, List<Long> skillIds) {
        Post post = new Post();
        post.setContent(content);
        post.setTimestamp(LocalDateTime.now());

        // Fetch the user
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        post.setUser(user);

        // Fetch skills
        List<Skill> skills = skillRepository.findAllById(skillIds);
        post.setSkills(new HashSet<>(skills));

        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }
    
    public Post createPostWithTags(Post post, List<Long> taggedUserIds) {
        List<User> taggedUsers = userRepository.findAllById(taggedUserIds);
        post.setTaggedUsers(taggedUsers);
        Post savedPost = postRepository.save(post);

        // Notify tagged users
        taggedUsers.forEach(user -> {
            notificationService.notifyUser(
                user.getId(),
                "You were tagged in a post by " + post.getUser().getFirstName()
            );
        });

        return savedPost;
    }

    public List<Map<String, Object>> getAllPostsWithTagsAndUsernames() {
        List<Post> posts = postRepository.findAllPostsWithTaggedUsers();
        return posts.stream().map(post -> {
            Map<String, Object> postMap = new HashMap<>();
            postMap.put("id", post.getId());
            postMap.put("content", post.getContent());
            postMap.put("creatorUsername", post.getUser().getFirstName());
            postMap.put("timestamp", post.getTimestamp());
            postMap.put("taggedUsers", post.getTaggedUsers().stream()
                    .map(user -> Map.of("id", user.getId(), "name", user.getFirstName()))
                    .toList());
            return postMap;
        }).toList();
    }

    
}