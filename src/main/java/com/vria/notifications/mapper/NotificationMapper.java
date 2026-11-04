package com.vria.notifications.mapper;

import com.vria.notifications.dto.NotificationDTO;
import com.vria.notifications.dto.NotificationPageDTO;
import com.vria.notifications.event.ActivityEvent;
import com.vria.notifications.model.Notification;
import com.vria.users.model.User;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.function.Predicate;

/**
 * Conversiones entre la entidad Notification, los eventos de actividad y los DTOs.
 */
@Component
public class NotificationMapper {

    private static final String SYSTEM_ACTOR = "Sistema";

    /**
     * @param actor usuario que realizo la accion, o null si la origino el sistema
     */
    public Notification toEntity(ActivityEvent event, String title, String message, User actor, Instant createdAt) {
        return Notification.builder()
                .module(event.module())
                .action(event.action())
                .entityId(event.entityId())
                .entityName(event.entityName())
                .title(title)
                .message(message)
                .actorId(actor != null ? actor.getIdUser() : null)
                .actorName(actor != null ? actor.fullName() : SYSTEM_ACTOR)
                .actorRole(actor != null ? actor.getRole() : null)
                .createdAt(createdAt)
                .build();
    }

    public NotificationDTO toDTO(Notification notification, boolean unread) {
        return new NotificationDTO(
                notification.getIdNotification(),
                notification.getModule(),
                notification.getAction(),
                notification.getEntityId(),
                notification.getEntityName(),
                notification.getTitle(),
                notification.getMessage(),
                notification.getActorId(),
                notification.getActorName(),
                notification.getActorRole(),
                notification.getCreatedAt(),
                unread
        );
    }

    public NotificationPageDTO toPageDTO(Page<Notification> page, Predicate<Notification> isUnread) {
        return new NotificationPageDTO(
                page.getContent().stream().map(n -> toDTO(n, isUnread.test(n))).toList(),
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.isLast()
        );
    }
}
