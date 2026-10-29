package com.vria.notifications.service;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityEvent;
import org.springframework.stereotype.Component;

/**
 * Construye los textos cortos que se muestran al usuario para cada evento.
 */
@Component
public class NotificationMessageFactory {

    private static final int MAX_MESSAGE_LENGTH = 255;

    public String title(NotificationModule module, NotificationAction action) {
        return switch (module) {
            case PRODUCT -> switch (action) {
                case CREATED -> "Nuevo producto";
                case STOCK_REDUCED -> "Salida de stock";
                case DELETED -> "Producto eliminado";
                default -> "Producto actualizado";
            };
            case CATEGORY -> switch (action) {
                case CREATED -> "Nueva categoría";
                case DELETED -> "Categoría eliminada";
                default -> "Categoría actualizada";
            };
            case TRANSFER -> switch (action) {
                case CREATED -> "Transferencia registrada";
                case DELETED -> "Transferencia eliminada";
                default -> "Transferencia actualizada";
            };
            case DELIVERY -> switch (action) {
                case CREATED -> "Nuevo domicilio";
                case STATE_CHANGED -> "Despacho actualizado";
                case DELETED -> "Domicilio eliminado";
                default -> "Domicilio actualizado";
            };
            case USER -> switch (action) {
                case CREATED -> "Nuevo usuario";
                case DELETED -> "Usuario eliminado";
                default -> "Usuario actualizado";
            };
        };
    }

    public String message(ActivityEvent event) {
        String message = event.summary();
        return message.length() > MAX_MESSAGE_LENGTH ? message.substring(0, MAX_MESSAGE_LENGTH - 1) + "…" : message;
    }
}
