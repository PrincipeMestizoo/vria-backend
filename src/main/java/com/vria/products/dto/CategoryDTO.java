package com.vria.products.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CategoryDTO(
        Long idCategory,

        @NotBlank(message = "El nombre de la categoria es obligatorio")
        String nameCategory,

        @NotNull(message = "El tipo de categoria es obligatorio")
        Long idTypeCategory,

        String nameTypeCategory
) {
}
