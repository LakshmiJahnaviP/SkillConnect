package com.example.skillConnectBackend.controller;

import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.service.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<Post> createPost(@RequestBody PostRequest postRequest) {
        Post post = postService.createPost(
            postRequest.getUserId(),
            postRequest.getContent(),
            postRequest.getSkillIds()
        );
        return ResponseEntity.ok(post);
    }
   

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getAllPosts() {
        List<Map<String, Object>> posts = postService.getAllPostsWithUsernames();
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<Post>> filterPosts(@RequestParam List<Long> skillIds) {
        List<Post> posts = postService.filterPostsBySkillIds(skillIds);
        return ResponseEntity.ok(posts);
    }
}
