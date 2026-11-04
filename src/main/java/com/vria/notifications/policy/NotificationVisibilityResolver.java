package com.vria.notifications.policy;

import com.vria.notifications.model.Notification;
import com.vria.users.enums.TypeRole;
import com.vria.users.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Selecciona la politica de visibilidad segun el rol del usuario autenticado.
 */
@Component
public class NotificationVisibilityResolver {

    private final Map<TypeRole, NotificationVisibilityPolicy> policies = new EnumMap<>(TypeRole.class);

    public NotificationVisibilityResolver(List<NotificationVisibilityPolicy> policies) {
        policies.forEach(policy -> this.policies.put(policy.role(), policy));
    }

    public Specification<Notification> visibleTo(User user) {
        NotificationVisibilityPolicy policy = policies.get(user.getRole());
        if (policy == null) {
            throw new AccessDeniedException("El rol " + user.getRole() + " no tiene acceso a notificaciones");
        }
        return policy.visibleTo(user);
    }
}
