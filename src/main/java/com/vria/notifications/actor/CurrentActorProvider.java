package com.vria.notifications.actor;

import com.vria.users.model.User;

import java.util.Optional;

/**
 * Abstrae de donde se obtiene el usuario que realiza una accion.
 */
public interface CurrentActorProvider {

    Optional<User> currentActor();
}
