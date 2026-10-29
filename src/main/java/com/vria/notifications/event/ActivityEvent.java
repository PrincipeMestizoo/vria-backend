package com.vria.notifications.event;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;

/**
 * Evento de dominio publicado por los servicios cuando un usuario modifica datos.
 * Los modulos solo publican el hecho; el modulo de notificaciones decide como registrarlo.
 *
 * @param entityName nombre legible de la entidad afectada (producto, cliente, etc.)
 * @param summary    frase corta en lenguaje natural que describe el cambio
 */
public record ActivityEvent(
        NotificationModule module,
        NotificationAction action,
        Long entityId,
        String entityName,
        String summary
) {
}
