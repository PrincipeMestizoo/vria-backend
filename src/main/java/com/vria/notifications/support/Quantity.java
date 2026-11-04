package com.vria.notifications.support;

/**
 * Cantidades en lenguaje natural para los mensajes de notificacion.
 */
public final class Quantity {

    private Quantity() {
    }

    public static String units(int amount) {
        return amount + (Math.abs(amount) == 1 ? " unidad" : " unidades");
    }
}
