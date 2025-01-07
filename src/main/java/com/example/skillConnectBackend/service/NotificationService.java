package com.example.skillConnectBackend.service;


import com.example.skillConnectBackend.model.Notification;
import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.model.User;
import com.example.skillConnectBackend.repository.NotificationRepository;
import com.example.skillConnectBackend.repository.UserRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public void createNotification(User user, String message) {
    	 Notification notification = new Notification();
         notification.setUser(user);
         notification.setMessage(message);
         notification.setRead(false); 
         notification.setTimestamp(LocalDateTime.now());
         notificationRepository.save(notification);
    }

    public List<Notification> getNotificationsForUser(Long userId) {
        return notificationRepository.findByUserIdAndReadFalse(userId);
    }

    public void markAllNotificationsAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndReadFalse(userId);
        for (Notification notification : notifications) {
            notification.setRead(true);
        }
        notificationRepository.saveAll(notifications);
    }
    
    @Transactional
    public void clearReadNotifications(Long userId) {
        notificationRepository.deleteReadNotificationsForUser(userId);
    }
}
