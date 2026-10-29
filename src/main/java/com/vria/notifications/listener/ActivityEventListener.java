package com.vria.notifications.listener;

import com.vria.notifications.event.ActivityEvent;
import com.vria.notifications.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Registra cada evento de actividad dentro de la misma transaccion que lo origino:
 * si la operacion falla no queda notificacion, y si el registro falla la operacion se revierte.
 */
@Component
@RequiredArgsConstructor
public class ActivityEventListener {

    private final NotificationService notificationService;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    public void onActivity(ActivityEvent event) {
        notificationService.record(event);
    }
}
