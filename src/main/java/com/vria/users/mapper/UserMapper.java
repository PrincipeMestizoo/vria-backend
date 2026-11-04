package com.vria.users.mapper;

import com.vria.users.dto.UserRequestDTO;
import com.vria.users.dto.UserResponseDTO;
import com.vria.users.model.User;
import org.springframework.stereotype.Component;

/**
 * Conversiones entre la entidad User y sus DTOs.
 * La contrasena llega ya codificada: el mapper no conoce el PasswordEncoder.
 */
@Component
public class UserMapper {

    public UserResponseDTO toResponse(User user) {
        return new UserResponseDTO(
                user.getIdUser(),
                user.getName(),
                user.getLastName(),
                user.getEnabled(),
                user.getEmail(),
                user.getRole()
        );
    }

    public User toEntity(UserRequestDTO dto, String encodedPassword) {
        return User.builder()
                .name(dto.name())
                .lastName(dto.lastName())
                .enabled(dto.enabled())
                .email(dto.email())
                .password(encodedPassword)
                .role(dto.role())
                .build();
    }

    // La contrasena se actualiza aparte porque es opcional al editar
    public void updateEntity(User user, UserRequestDTO dto) {
        user.setName(dto.name());
        user.setLastName(dto.lastName());
        user.setEnabled(dto.enabled());
        user.setEmail(dto.email());
        user.setRole(dto.role());
    }
}
