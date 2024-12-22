package com.example.skillConnectBackend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "notifications")
public class Notification {

	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    @ManyToOne
	    @JoinColumn(name = "user_id", nullable = false)
	    private User user;

	    private String message;

	    @Column(name = "is_read")
	    private boolean read;

	    private LocalDateTime timestamp;

	    // Getters and Setters
	    

	    public String getMessage() {
	        return message;
	    }

	    public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}

		public void setMessage(String message) {
	        this.message = message;
	    }

	   

	    public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public boolean isRead() {
			return read;
		}

		public void setRead(boolean read) {
			this.read = read;
		}

		public LocalDateTime getTimestamp() {
	        return timestamp;
	    }

	    public void setTimestamp(LocalDateTime timestamp) {
	        this.timestamp = timestamp;
	    }
	}
