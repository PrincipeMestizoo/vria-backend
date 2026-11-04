package com.vria.notifications.support;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;

/**
 * Define el dia vigente de las notificaciones segun la zona horaria del negocio.
 * Solo se muestran los eventos del dia en curso; al terminar el dia se eliminan.
 */
@Component
public class NotificationDay {

    private final ZoneId zone;

    public NotificationDay(@Value("${vria.notifications.zone}") String zone) {
        this.zone = ZoneId.of(zone);
    }

    public Instant startOfToday() {
        return LocalDate.now(zone).atStartOfDay(zone).toInstant();
    }
}
