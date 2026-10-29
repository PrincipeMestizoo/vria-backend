package com.vria.notifications.service;

import com.vria.users.model.User;

import java.util.Optional;

/**
 * Abstrae de donde se obtiene el usuario que realiza una accion.
 */
public interface CurrentActorProvider {

    Optional<User> currentActor();
}
