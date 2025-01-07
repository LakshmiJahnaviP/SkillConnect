package com.example.skillConnectBackend.requests;

import jakarta.validation.constraints.NotNull;

public class LoginRequest {
	@NotNull(message = "Identifier is required")
	private String identifier;  
	
	@NotNull(message = "Password is required")
    private String password;

    
    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
