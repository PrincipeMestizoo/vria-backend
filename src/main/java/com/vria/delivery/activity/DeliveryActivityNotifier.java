package com.vria.delivery.activity;

import com.vria.delivery.enums.StateDelivery;
import com.vria.delivery.model.Delivery;
import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Redacta y publica los movimientos realizados sobre domicilios.
 */
@Component
@RequiredArgsConstructor
public class DeliveryActivityNotifier {

    private final ActivityPublisher activityPublisher;

    public void created(Delivery delivery) {
        publish(NotificationAction.CREATED, delivery, "Se creó un domicilio para " + delivery.getNameClient()
                + " (" + label(delivery.getState()) + ").");
    }

    public void stateChanged(StateDelivery previous, Delivery delivery) {
        publish(NotificationAction.STATE_CHANGED, delivery, "El domicilio de " + delivery.getNameClient() + " pasó de "
                + label(previous) + " a " + label(delivery.getState()) + ".");
    }

    public void deleted(Delivery delivery) {
        publish(NotificationAction.DELETED, delivery, "Se eliminó el domicilio de " + delivery.getNameClient() + ".");
    }

    private void publish(NotificationAction action, Delivery delivery, String summary) {
        activityPublisher.publish(NotificationModule.DELIVERY, action,
                delivery.getIdDelivery(), delivery.getNameClient(), summary);
    }

    private static String label(StateDelivery state) {
        return state.getLabel().toLowerCase();
    }
}
