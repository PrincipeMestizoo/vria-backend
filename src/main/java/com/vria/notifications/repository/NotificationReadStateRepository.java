package com.vria.notifications.repository;

import com.vria.notifications.model.NotificationReadState;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationReadStateRepository extends JpaRepository<NotificationReadState, Long> {
}
