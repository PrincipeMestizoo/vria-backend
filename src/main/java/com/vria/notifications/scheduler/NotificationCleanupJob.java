package com.vria.notifications.scheduler;

import com.vria.notifications.repository.NotificationRepository;
import com.vria.notifications.support.NotificationDay;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Al iniciar cada dia elimina las notificaciones de los dias anteriores.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationCleanupJob {

    private final NotificationRepository notificationRepository;
    private final NotificationDay notificationDay;

    @Scheduled(cron = "0 0 0 * * *", zone = "${vria.notifications.zone}")
    @Transactional
    public void purgePreviousDays() {
        int deleted = notificationRepository.deleteCreatedBefore(notificationDay.startOfToday());
        log.info("Notificaciones de dias anteriores eliminadas: {}", deleted);
    }
}
