package com.vria.notifications.policy;

import com.vria.notifications.model.Notification;
import com.vria.users.enums.TypeRole;
import com.vria.users.model.User;
import org.springframework.data.jpa.domain.Specification;

/**
 * Define que notificaciones puede ver un rol. Cada rol tiene su propia implementacion,
 * por lo que agregar un rol nuevo no requiere modificar el servicio (principio abierto/cerrado).
 */
public interface NotificationVisibilityPolicy {

    TypeRole role();

    Specification<Notification> visibleTo(User user);
}
