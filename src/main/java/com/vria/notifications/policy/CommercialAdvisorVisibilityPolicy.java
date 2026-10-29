package com.vria.notifications.policy;

import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.model.Notification;
import com.vria.users.enums.TypeRole;
import com.vria.users.model.User;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Set;

import static com.vria.notifications.policy.NotificationSpecifications.inModules;
import static com.vria.notifications.policy.NotificationSpecifications.performedBy;

/**
 * El asesor ve los cambios del catalogo y unicamente sus propias
 * transferencias y domicilios.
 */
@Component
public class CommercialAdvisorVisibilityPolicy implements NotificationVisibilityPolicy {

    private static final Set<NotificationModule> OWN_MODULES = Set.of(NotificationModule.TRANSFER, NotificationModule.DELIVERY);

    @Override
    public TypeRole role() {
        return TypeRole.COMMERCIAL_ADVISOR;
    }

    @Override
    public Specification<Notification> visibleTo(User user) {
        return inModules(NotificationModule.catalog())
                .or(inModules(OWN_MODULES).and(performedBy(user.getIdUser())));
    }
}
