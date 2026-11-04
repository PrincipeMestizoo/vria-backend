package com.vria.auth.mapper;

import com.vria.auth.dto.AuthResponse;
import com.vria.auth.dto.RegisterRequest;
import com.vria.users.model.User;
import org.springframework.stereotype.Component;

/**
 * Conversiones entre los DTOs de autenticacion y la entidad User.
 */
@Component
public class AuthMapper {

    public AuthResponse toAuthResponse(User user) {
        return AuthResponse.builder()
                .idUser(user.getIdUser())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    // Los usuarios que se registran quedan habilitados de inmediato
    public User toEntity(RegisterRequest request, String encodedPassword) {
        return User.builder()
                .name(request.name())
                .lastName(request.lastName())
                .email(request.email())
                .password(encodedPassword)
                .role(request.role())
                .enabled(true)
                .build();
    }
}
