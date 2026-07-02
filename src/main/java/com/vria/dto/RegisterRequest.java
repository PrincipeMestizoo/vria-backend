package com.vria.dto;

import com.vria.model.Rol;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank String username,
        @NotBlank @Size(min = 8, message = "La contrasena debe tener minimo 8 caracteres")
        String password,
        @NotBlank String nombreCompleto,
        @NotNull Rol rol
) {
}
