package com.vria.auth.dto;

/**
 * Resultado interno de una autenticacion: tokens (que se escriben en cookies)
 * y los datos del usuario (que se devuelven en el body).
 */
public record AuthSession(
        String accessToken,
        String refreshToken,
        AuthResponse user
) {
}
