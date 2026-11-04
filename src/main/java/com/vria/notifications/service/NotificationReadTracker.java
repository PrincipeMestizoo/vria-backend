package com.vria.notifications.service;

import com.vria.notifications.model.Notification;
import com.vria.notifications.model.NotificationReadState;
import com.vria.notifications.repository.NotificationReadStateRepository;
import com.vria.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Lleva el control de hasta donde reviso cada usuario sus notificaciones.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationReadTracker {

    private final NotificationReadStateRepository readStateRepository;

    public void markAllAsRead(User user) {
        readStateRepository.save(new NotificationReadState(user.getIdUser(), Instant.now()));
    }

    @Transactional(readOnly = true)
    public Optional<Instant> lastReadAt(User user) {
        return readStateRepository.findById(user.getIdUser()).map(NotificationReadState::getLastReadAt);
    }

    /**
     * Indica si una notificacion esta sin leer para el usuario.
     * Las acciones propias nunca se marcan como no leidas: el usuario ya sabe lo que hizo.
     */
    @Transactional(readOnly = true)
    public Predicate<Notification> unreadFor(User viewer) {
        Instant lastReadAt = lastReadAt(viewer).orElse(null);
        return notification -> !viewer.getIdUser().equals(notification.getActorId())
                && (lastReadAt == null || notification.getCreatedAt().isAfter(lastReadAt));
    }
}
