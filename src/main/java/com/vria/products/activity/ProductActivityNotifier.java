package com.vria.products.activity;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityPublisher;
import com.vria.notifications.support.Money;
import com.vria.notifications.support.Quantity;
import com.vria.products.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Redacta y publica los movimientos realizados sobre productos.
 */
@Component
@RequiredArgsConstructor
public class ProductActivityNotifier {

    private final ActivityPublisher activityPublisher;

    public void created(Product product) {
        publish(NotificationAction.CREATED, product,
                "Se agregó " + product.getNameProduct() + " al inventario con " + Quantity.units(product.getStock()) + ".");
    }

    public void updated(Product previous, Product current) {
        publish(NotificationAction.UPDATED, current, describeChanges(previous, current));
    }

    public void stockReduced(Product product, int quantity) {
        publish(NotificationAction.STOCK_REDUCED, product, (quantity == 1 ? "Salió " : "Salieron ") + Quantity.units(quantity)
                + " de " + product.getNameProduct() + ". Quedan " + product.getStock() + ".");
    }

    public void deleted(Product product) {
        publish(NotificationAction.DELETED, product, "Se eliminó " + product.getNameProduct() + " del inventario.");
    }

    private void publish(NotificationAction action, Product product, String summary) {
        activityPublisher.publish(NotificationModule.PRODUCT, action,
                product.getIdProduct(), product.getNameProduct(), summary);
    }

    // Describe en una frase solo los cambios relevantes para inventario y ventas
    private static String describeChanges(Product previous, Product current) {
        List<String> changes = new ArrayList<>();
        if (!previous.getNameProduct().equals(current.getNameProduct())) {
            changes.add("antes se llamaba " + previous.getNameProduct());
        }
        if (previous.getStock() != current.getStock()) {
            changes.add("ahora hay " + Quantity.units(current.getStock()) + " (antes " + previous.getStock() + ")");
        }
        if (Float.compare(previous.getPrice(), current.getPrice()) != 0) {
            changes.add("el precio pasó de " + Money.format(previous.getPrice()) + " a " + Money.format(current.getPrice()));
        }
        if (changes.isEmpty()) {
            return "Se actualizó la información de " + current.getNameProduct() + ".";
        }
        return "Se actualizó " + current.getNameProduct() + ": " + joinNatural(changes) + ".";
    }

    private static String joinNatural(List<String> parts) {
        if (parts.size() == 1) {
            return parts.getFirst();
        }
        return String.join(", ", parts.subList(0, parts.size() - 1)) + " y " + parts.getLast();
    }
}
