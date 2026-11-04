package com.vria.notifications.service;

import com.vria.notifications.actor.CurrentActorProvider;
import com.vria.notifications.event.ActivityEvent;
import com.vria.notifications.mapper.NotificationMapper;
import com.vria.notifications.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

/**
 * Persiste cada evento de actividad como una notificacion del historial.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class NotificationRecorder {

    private final NotificationRepository notificationRepository;
    private final NotificationMessageFactory messageFactory;
    private final NotificationMapper notificationMapper;
    private final CurrentActorProvider actorProvider;

    public void record(ActivityEvent event) {
        notificationRepository.save(notificationMapper.toEntity(
                event,
                messageFactory.title(event.module(), event.action()),
                messageFactory.message(event),
                actorProvider.currentActor().orElse(null),
                Instant.now()
        ));
    }
}
