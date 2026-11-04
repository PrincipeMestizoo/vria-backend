package com.vria.notifications.dto;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.users.enums.TypeRole;

import java.time.Instant;

public record NotificationDTO(
        Long idNotification,
        NotificationModule module,
        NotificationAction action,
        Long entityId,
        String entityName,
        String title,
        String message,
        Long actorId,
        String actorName,
        TypeRole actorRole,
        Instant createdAt,
        boolean unread
) {
}
