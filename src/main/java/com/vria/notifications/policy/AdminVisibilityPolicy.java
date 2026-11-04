package com.vria.notifications.policy;

import com.vria.notifications.model.Notification;
import com.vria.users.enums.TypeRole;
import com.vria.users.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * El administrador ve todos los movimientos de la plataforma.
 */
@Component
public class AdminVisibilityPolicy implements NotificationVisibilityPolicy {

    @Override
    public TypeRole role() {
        return TypeRole.ADMIN;
    }

    @Override
    public Specification<Notification> visibleTo(User user) {
        return NotificationSpecifications.any();
    }
}
