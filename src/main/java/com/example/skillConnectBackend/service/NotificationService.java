package com.example.skillConnectBackend.service;


import com.example.skillConnectBackend.model.Notification;
import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.NotificationRepository;
import com.example.skillConnectBackend.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;
    
    @Autowired
    private UserRepository userRepository;

    /**
     * Get all notifications.
     *
     * @return List of all notifications.
     */
    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    /**
     * Get all unread notifications.
     *
     * @return List of unread notifications.
     */
    public List<Notification> getUnreadNotifications() {
        return notificationRepository.findByReadFalse();
    }

    /**
     * Get all notifications for a specific user.
     *
     * @param userId The user ID.
     * @return List of notifications for the user.
     */
    public List<Notification> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId);
    }

    /**
     * Mark all notifications as read.
     */
    public void markAllAsRead() {
        List<Notification> unreadNotifications = notificationRepository.findByReadFalse();
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Mark notifications as read for a specific user.
     *
     * @param userId The user ID.
     */
    public void markAllAsReadByUserId(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(userId);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(unreadNotifications);
    }

    /**
     * Add a new notification.
     *
     * @param notification The notification to add.
     */
    public void addNotification(Notification notification) {
        notificationRepository.save(notification);
    }
    
    public void notifyUser(Long userId, String message) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setTimestamp(LocalDateTime.now());

        notificationRepository.save(notification);
    }
    public void notifyTaggedUsers(Post post) {
        post.getTaggedUsers().forEach(user -> {
            String message = "You were mentioned in a post by " + post.getUser().getFirstName();
            notifyUser(user.getId(), message);
        });
    }

    public void markUserNotificationsAsRead(Long userId) {
        List<Notification> unreadNotifications = notificationRepository.findByUserIdAndReadFalse(userId);
        for (Notification notification : unreadNotifications) {
            notification.setRead(true); // Mark each notification as read
        }
        notificationRepository.saveAll(unreadNotifications); // Save changes
    }

    
    
}

