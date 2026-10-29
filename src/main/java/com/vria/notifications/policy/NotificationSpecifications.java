package com.vria.notifications.policy;

import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.model.Notification;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.Set;

/**
 * Filtros reutilizables para consultar notificaciones.
 */
public final class NotificationSpecifications {

    private NotificationSpecifications() {
    }

    public static Specification<Notification> any() {
        return (root, query, cb) -> cb.conjunction();
    }

    public static Specification<Notification> inModules(Set<NotificationModule> modules) {
        return (root, query, cb) -> root.get("module").in(modules);
    }

    public static Specification<Notification> ofModule(NotificationModule module) {
        return (root, query, cb) -> cb.equal(root.get("module"), module);
    }

    public static Specification<Notification> performedBy(Long idUser) {
        return (root, query, cb) -> cb.equal(root.get("actorId"), idUser);
    }

    public static Specification<Notification> notPerformedBy(Long idUser) {
        return (root, query, cb) -> cb.or(cb.isNull(root.get("actorId")), cb.notEqual(root.get("actorId"), idUser));
    }

    public static Specification<Notification> createdSince(Instant instant) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdAt"), instant);
    }

    public static Specification<Notification> createdAfter(Instant instant) {
        return (root, query, cb) -> cb.greaterThan(root.get("createdAt"), instant);
    }
}
