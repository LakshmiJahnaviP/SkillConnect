package com.example.skillConnectBackend.responses;

import com.example.skillConnectBackend.model.User;

public class SuccessResponse {
    private String message;
    private User user;
    private String firstName;

    public SuccessResponse(String message, User user, String firstName) {
        this.message = message;
        this.user = user;
        this.firstName = firstName;
        
    }

    public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	// Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
