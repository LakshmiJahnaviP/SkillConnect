package com.example.skillConnectBackend.responses;

import java.time.LocalDateTime;
import java.util.List;

import com.example.skillConnectBackend.model.Skill;

public class PostResponse {
    private Long id;
    private String content;
    private LocalDateTime timestamp;
    private String username; 
    private List<Skill> skills;

    public PostResponse(Long id, String content, LocalDateTime timestamp, String username, List<Skill> skills) {
        this.id = id;
        this.content = content;
        this.timestamp = timestamp;
        this.username = username;
        this.skills = skills;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Skill> getSkills() {
		return skills;
	}

	public void setSkills(List<Skill> skills) {
		this.skills = skills;
	}

    // Getters and setters
    
}
