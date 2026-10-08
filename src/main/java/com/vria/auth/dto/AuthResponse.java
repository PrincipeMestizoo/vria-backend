package com.vria.auth.dto;

import com.vria.users.enums.TypeRole;
import lombok.Builder;

/**
 * Datos del usuario autenticado. Los tokens NO viajan en el body:
 * se envian como cookies HttpOnly (ver AuthCookieService).
 */
@Builder
public record AuthResponse(
        Long idUser,
        String name,
        String email,
        TypeRole role
) {
}
