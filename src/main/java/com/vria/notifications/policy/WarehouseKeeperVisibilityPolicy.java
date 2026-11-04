package com.vria.notifications.policy;

import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.model.Notification;
import com.vria.users.enums.TypeRole;
import com.vria.users.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

/**
 * El bodeguero ve todos los eventos del catalogo (productos y categorias),
 * sin importar quien los haya realizado.
 */
@Component
public class WarehouseKeeperVisibilityPolicy implements NotificationVisibilityPolicy {

    @Override
    public TypeRole role() {
        return TypeRole.WAREHOUSE_KEEPER;
    }

    @Override
    public Specification<Notification> visibleTo(User user) {
        return NotificationSpecifications.inModules(NotificationModule.catalog());
    }
}
