package com.example.skillConnectBackend.controller;

import com.example.skillConnectBackend.model.Notification;
import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.service.NotificationService;
import com.example.skillConnectBackend.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;



@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;


    @GetMapping
    public ResponseEntity<List<Notification>> getUserNotifications(@RequestParam Long userId) {
        List<Notification> notifications = notificationService.getNotificationsForUser(userId);
        return ResponseEntity.ok(notifications);
    }

    @PostMapping("/mark-read")
    public ResponseEntity<Void> markNotificationsAsRead(@RequestParam Long userId) {
        notificationService.markAllNotificationsAsRead(userId);
        return ResponseEntity.ok().build();
    }
    
    @DeleteMapping("/clear")
    public ResponseEntity<Void> clearReadNotifications(@RequestParam Long recipientId) {
        notificationService.clearReadNotifications(recipientId);
        return ResponseEntity.ok().build();
    }
    
}