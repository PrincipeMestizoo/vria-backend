package com.vria.products.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryDTO(
        Long idCategory,

        @NotBlank(message = "El nombre de la categoria es obligatorio")
        String nameCategory
) {
}
