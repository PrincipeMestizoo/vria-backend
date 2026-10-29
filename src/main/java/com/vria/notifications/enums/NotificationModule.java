package com.vria.notifications.enums;

import java.util.Set;

/**
 * Modulo de la plataforma donde ocurrio el evento.
 */
public enum NotificationModule {
    PRODUCT,
    CATEGORY,
    TRANSFER,
    DELIVERY,
    USER;

    private static final Set<NotificationModule> CATALOG = Set.of(PRODUCT, CATEGORY);

    /** Modulos que forman el catalogo de productos. */
    public static Set<NotificationModule> catalog() {
        return CATALOG;
    }
}
