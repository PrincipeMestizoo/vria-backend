package com.vria.notifications.support;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formato de montos en pesos para los mensajes de notificacion.
 */
public final class Money {

    private static final Locale COLOMBIA = Locale.forLanguageTag("es-CO");

    private Money() {
    }

    public static String format(float amount) {
        NumberFormat format = NumberFormat.getIntegerInstance(COLOMBIA);
        return "$" + format.format(Math.round(amount));
    }
}
