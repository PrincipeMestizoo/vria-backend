package com.vria.users.activity;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityPublisher;
import com.vria.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Redacta y publica los movimientos realizados sobre usuarios.
 */
@Component
@RequiredArgsConstructor
public class UserActivityNotifier {

    private final ActivityPublisher activityPublisher;

    public void created(User user) {
        publish(NotificationAction.CREATED, user,
                "Se creó el usuario " + user.fullName() + " como " + user.getRole().getLabel() + ".");
    }

    public void updated(User user) {
        publish(NotificationAction.UPDATED, user, "Se actualizaron los datos de " + user.fullName() + ".");
    }

    public void deleted(User user) {
        publish(NotificationAction.DELETED, user, "Se eliminó el usuario " + user.fullName() + ".");
    }

    private void publish(NotificationAction action, User user, String summary) {
        activityPublisher.publish(NotificationModule.USER, action, user.getIdUser(), user.fullName(), summary);
    }
}
