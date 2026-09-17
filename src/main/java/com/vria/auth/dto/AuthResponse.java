package com.vria.auth.dto;

import com.vria.users.enums.TypeRole;
import lombok.Builder;

@Builder
public record AuthResponse(
        String accessToken,
        String refreshToken,
        String tokenType,
        Long idUser,
        String name,
        String email,
        TypeRole role
) {
}
