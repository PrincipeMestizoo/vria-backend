package com.vria.notifications.model;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.users.enums.TypeRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Registro historico de un evento realizado en la plataforma.
 * Los datos del autor se guardan como copia (sin llave foranea) para que el
 * historial se conserve aunque el usuario sea eliminado.
 */
@Entity
@Table(name = "notifications", indexes = {
        @Index(name = "idx_notifications_created_at", columnList = "created_at"),
        @Index(name = "idx_notifications_module", columnList = "module")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notification")
    private Long idNotification;

    @Enumerated(EnumType.STRING)
    @Column(name = "module", nullable = false, length = 20)
    private NotificationModule module;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false, length = 20)
    private NotificationAction action;

    @Column(name = "entity_id")
    private Long entityId;

    @Column(name = "entity_name", length = 201)
    private String entityName;

    @Column(name = "title", nullable = false, length = 120)
    private String title;

    @Column(name = "message", nullable = false, length = 255)
    private String message;

    @Column(name = "actor_id")
    private Long actorId;

    @Column(name = "actor_name", nullable = false, length = 201)
    private String actorName;

    @Enumerated(EnumType.STRING)
    @Column(name = "actor_role", length = 30)
    private TypeRole actorRole;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;
}
