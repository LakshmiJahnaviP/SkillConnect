package com.example.skillConnectBackend.controller;

import com.example.skillConnectBackend.model.Post;
import com.example.skillConnectBackend.service.NotificationService;
import com.example.skillConnectBackend.service.PostService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import javax.management.Notification;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public List<com.example.skillConnectBackend.model.Notification> getNotifications() {
        return notificationService.getAllNotifications();
    }

    @PostMapping("/mark-read")
    public ResponseEntity<String> markAllAsRead() {
        notificationService.markAllAsRead();
        return ResponseEntity.ok("All notifications marked as read");
    }
}
