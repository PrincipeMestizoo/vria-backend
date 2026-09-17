package com.vria.users.dto;

import com.vria.users.enums.TypeRole;

/**
 * DTO de salida: nunca expone la contrasena.
 */
public record UserResponseDTO(
        Long idUser,
        String name,
        String lastName,
        Boolean enabled,
        String email,
        TypeRole role
) {
}
