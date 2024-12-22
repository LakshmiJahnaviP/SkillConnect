package com.example.skillConnectBackend.repository;

import com.example.skillConnectBackend.model.Notification;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {



	 List<Notification> findByUserIdAndReadFalse(Long userId);

	    @Transactional
	    @Modifying
	    @Query("DELETE FROM Notification n WHERE n.user.id = :userId AND n.read = true")
	    void deleteByUserIdAndReadTrue(@Param("userId") Long userId);
	
}
