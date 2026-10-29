package com.vria.notifications.service;

import com.vria.notifications.dto.NotificationDTO;
import com.vria.notifications.dto.NotificationPageDTO;
import com.vria.notifications.dto.UnreadCountDTO;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityEvent;
import com.vria.notifications.model.Notification;
import com.vria.notifications.model.NotificationReadState;
import com.vria.notifications.policy.NotificationVisibilityResolver;
import com.vria.notifications.repository.NotificationReadStateRepository;
import com.vria.notifications.repository.NotificationRepository;
import com.vria.notifications.support.NotificationDay;
import com.vria.users.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

import static com.vria.notifications.policy.NotificationSpecifications.createdAfter;
import static com.vria.notifications.policy.NotificationSpecifications.createdSince;
import static com.vria.notifications.policy.NotificationSpecifications.notPerformedBy;
import static com.vria.notifications.policy.NotificationSpecifications.ofModule;

@Service
@RequiredArgsConstructor
@Transactional
public class NotificationService {

    private static final int MAX_PAGE_SIZE = 100;
    private static final String SYSTEM_ACTOR = "Sistema";

    private final NotificationRepository notificationRepository;
    private final NotificationReadStateRepository readStateRepository;
    private final NotificationVisibilityResolver visibilityResolver;
    private final NotificationMessageFactory messageFactory;
    private final CurrentActorProvider actorProvider;
    private final NotificationDay notificationDay;

    public void record(ActivityEvent event) {
        Optional<User> actor = actorProvider.currentActor();
        Notification notification = Notification.builder()
                .module(event.module())
                .action(event.action())
                .entityId(event.entityId())
                .entityName(event.entityName())
                .title(messageFactory.title(event.module(), event.action()))
                .message(messageFactory.message(event))
                .actorId(actor.map(User::getIdUser).orElse(null))
                .actorName(actor.map(u -> u.getName() + " " + u.getLastName()).orElse(SYSTEM_ACTOR))
                .actorRole(actor.map(User::getRole).orElse(null))
                .createdAt(Instant.now())
                .build();
        notificationRepository.save(notification);
    }

    @Transactional(readOnly = true)
    public NotificationPageDTO findVisible(User user, NotificationModule module, int page, int size) {
        Specification<Notification> spec = visibleToday(user);
        if (module != null) {
            spec = spec.and(ofModule(module));
        }
        PageRequest pageable = PageRequest.of(Math.max(page, 0), Math.clamp(size, 1, MAX_PAGE_SIZE),
                Sort.by(Sort.Direction.DESC, "createdAt", "idNotification"));
        Page<Notification> result = notificationRepository.findAll(spec, pageable);

        Instant lastReadAt = lastReadAt(user);
        return new NotificationPageDTO(
                result.getContent().stream().map(n -> toDTO(n, user, lastReadAt)).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.isLast()
        );
    }

    @Transactional(readOnly = true)
    public UnreadCountDTO countUnread(User user) {
        Specification<Notification> spec = visibleToday(user).and(notPerformedBy(user.getIdUser()));
        Instant lastReadAt = lastReadAt(user);
        if (lastReadAt != null) {
            spec = spec.and(createdAfter(lastReadAt));
        }
        return new UnreadCountDTO(notificationRepository.count(spec));
    }

    public void markAllAsRead(User user) {
        readStateRepository.save(new NotificationReadState(user.getIdUser(), Instant.now()));
    }

    // Se filtra por dia aunque la limpieza nocturna no se haya ejecutado (por ejemplo, si el servidor estaba apagado)
    private Specification<Notification> visibleToday(User user) {
        return visibilityResolver.visibleTo(user).and(createdSince(notificationDay.startOfToday()));
    }

    private Instant lastReadAt(User user) {
        return readStateRepository.findById(user.getIdUser())
                .map(NotificationReadState::getLastReadAt)
                .orElse(null);
    }

    // Las acciones propias nunca se marcan como no leidas: el usuario ya sabe lo que hizo
    private NotificationDTO toDTO(Notification n, User viewer, Instant lastReadAt) {
        boolean ownAction = viewer.getIdUser().equals(n.getActorId());
        boolean unread = !ownAction && (lastReadAt == null || n.getCreatedAt().isAfter(lastReadAt));
        return new NotificationDTO(
                n.getIdNotification(),
                n.getModule(),
                n.getAction(),
                n.getEntityId(),
                n.getEntityName(),
                n.getTitle(),
                n.getMessage(),
                n.getActorId(),
                n.getActorName(),
                n.getActorRole(),
                n.getCreatedAt(),
                unread
        );
    }
}
