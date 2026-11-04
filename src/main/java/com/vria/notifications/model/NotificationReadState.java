package com.vria.notifications.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

/**
 * Marca hasta que momento un usuario reviso sus notificaciones.
 * Todo evento posterior a lastReadAt se considera no leido.
 */
@Entity
@Table(name = "notification_read_states")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationReadState {

    @Id
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "last_read_at", nullable = false)
    private Instant lastReadAt;
}
