package com.example.skillConnectBackend.repository;

import com.example.skillConnectBackend.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    /**
     * Find all unread notifications.
     *
     * @return List of unread notifications
     */
    List<Notification> findByReadFalse();

    /**
     * Find notifications by user.
     *
     * @param userId The user's ID
     * @return List of notifications for the user
     */
    List<Notification> findByUserId(Long userId);

	List<Notification> findByUserIdAndReadFalse(Long userId);
	
}
