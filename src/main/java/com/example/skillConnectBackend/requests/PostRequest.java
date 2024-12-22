package com.example.skillConnectBackend.requests;
import java.util.List;

public class PostRequest {
    private Long userId;
    private String content;
    private List<Long> skillIds;
    private List<Long> taggedUserIds;

    

	// Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<Long> getSkillIds() {
        return skillIds;
    }

    public void setSkillIds(List<Long> skillIds) {
        this.skillIds = skillIds;
    }
    public List<Long> getTaggedUserIds() {
		return taggedUserIds;
	}

	public void setTaggedUserIds(List<Long> taggedUserIds) {
		this.taggedUserIds = taggedUserIds;
	}
}
