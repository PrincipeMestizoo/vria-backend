package com.vria.users.dto;

import com.vria.users.enums.TypeRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO usado para crear/actualizar un usuario desde el modulo de administracion.
 * Para el registro publico se usa RegisterRequest (modulo auth).
 */
public record UserRequestDTO(

        @NotBlank(message = "El nombre es obligatorio")
        String name,

        @NotBlank(message = "El apellido es obligatorio")
        String lastName,

        @NotNull(message = "Debe indicar si el usuario esta habilitado")
        Boolean enabled,

        @NotBlank(message = "El correo es obligatorio")
        @Email(message = "El correo no tiene un formato valido")
        String email,

        @Size(min = 8, message = "La contrasena debe tener al menos 8 caracteres")
        String password,

        @NotNull(message = "El rol es obligatorio")
        TypeRole role
) {
}
