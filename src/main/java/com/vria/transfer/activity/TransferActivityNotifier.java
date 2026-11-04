package com.vria.transfer.activity;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityPublisher;
import com.vria.notifications.support.Money;
import com.vria.transfer.model.Transfer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Redacta y publica los movimientos realizados sobre transferencias.
 */
@Component
@RequiredArgsConstructor
public class TransferActivityNotifier {

    private final ActivityPublisher activityPublisher;

    public void created(Transfer transfer) {
        publish(NotificationAction.CREATED, transfer, "Se registró una transferencia de " + Money.format(transfer.getAmount())
                + " de " + transfer.getNameClient() + " por " + transfer.getBank() + ".");
    }

    public void deleted(Transfer transfer) {
        publish(NotificationAction.DELETED, transfer, "Se eliminó la transferencia de " + Money.format(transfer.getAmount())
                + " de " + transfer.getNameClient() + ".");
    }

    private void publish(NotificationAction action, Transfer transfer, String summary) {
        activityPublisher.publish(NotificationModule.TRANSFER, action,
                transfer.getIdTransfer(), transfer.getNameClient(), summary);
    }
}
