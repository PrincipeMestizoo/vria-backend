package com.vria.notifications.service;

import com.vria.notifications.dto.NotificationPageDTO;
import com.vria.notifications.dto.UnreadCountDTO;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.mapper.NotificationMapper;
import com.vria.notifications.model.Notification;
import com.vria.notifications.policy.NotificationVisibilityResolver;
import com.vria.notifications.repository.NotificationRepository;
import com.vria.notifications.support.NotificationDay;
import com.vria.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

import static com.vria.notifications.policy.NotificationSpecifications.createdAfter;
import static com.vria.notifications.policy.NotificationSpecifications.createdSince;
import static com.vria.notifications.policy.NotificationSpecifications.notPerformedBy;
import static com.vria.notifications.policy.NotificationSpecifications.ofModule;

/**
 * Consultas del historial de notificaciones visible para cada usuario.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NotificationQueryService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final Sort NEWEST_FIRST = Sort.by(Sort.Direction.DESC, "createdAt", "idNotification");

    private final NotificationRepository notificationRepository;
    private final NotificationVisibilityResolver visibilityResolver;
    private final NotificationReadTracker readTracker;
    private final NotificationMapper notificationMapper;
    private final NotificationDay notificationDay;

    public NotificationPageDTO findVisible(User user, NotificationModule module, int page, int size) {
        Specification<Notification> spec = visibleToday(user);
        if (module != null) {
            spec = spec.and(ofModule(module));
        }
        Pageable pageable = PageRequest.of(Math.max(page, 0), Math.clamp(size, 1, MAX_PAGE_SIZE), NEWEST_FIRST);
        return notificationMapper.toPageDTO(notificationRepository.findAll(spec, pageable), readTracker.unreadFor(user));
    }

    public UnreadCountDTO countUnread(User user) {
        Specification<Notification> spec = visibleToday(user).and(notPerformedBy(user.getIdUser()));
        Instant lastReadAt = readTracker.lastReadAt(user).orElse(null);
        if (lastReadAt != null) {
            spec = spec.and(createdAfter(lastReadAt));
        }
        return new UnreadCountDTO(notificationRepository.count(spec));
    }

    // Se filtra por dia aunque la limpieza nocturna no se haya ejecutado (por ejemplo, si el servidor estaba apagado)
    private Specification<Notification> visibleToday(User user) {
        return visibilityResolver.visibleTo(user).and(createdSince(notificationDay.startOfToday()));
    }
}
