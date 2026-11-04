package com.vria.products.activity;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityPublisher;
import com.vria.products.model.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Redacta y publica los movimientos realizados sobre categorias.
 */
@Component
@RequiredArgsConstructor
public class CategoryActivityNotifier {

    private final ActivityPublisher activityPublisher;

    public void created(Category category) {
        publish(NotificationAction.CREATED, category, "Se creó la categoría " + category.getNameCategory() + ".");
    }

    public void updated(String previousName, Category category) {
        String currentName = category.getNameCategory();
        publish(NotificationAction.UPDATED, category, previousName.equals(currentName)
                ? "Se actualizó la categoría " + currentName + "."
                : "La categoría " + previousName + " ahora se llama " + currentName + ".");
    }

    public void deleted(Category category) {
        publish(NotificationAction.DELETED, category, "Se eliminó la categoría " + category.getNameCategory() + ".");
    }

    private void publish(NotificationAction action, Category category, String summary) {
        activityPublisher.publish(NotificationModule.CATEGORY, action,
                category.getIdCategory(), category.getNameCategory(), summary);
    }
}
