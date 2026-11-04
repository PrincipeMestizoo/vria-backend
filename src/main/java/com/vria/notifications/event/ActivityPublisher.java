package com.vria.notifications.event;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Punto unico para que los modulos publiquen eventos de actividad
 * sin conocer como se construye el ActivityEvent.
 */
@Component
@RequiredArgsConstructor
public class ActivityPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publish(NotificationModule module, NotificationAction action,
                        Long entityId, String entityName, String summary) {
        eventPublisher.publishEvent(new ActivityEvent(module, action, entityId, entityName, summary));
    }
}
