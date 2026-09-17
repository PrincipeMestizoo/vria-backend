package com.vria.products.dto;

import jakarta.validation.constraints.NotBlank;

public record TypeCategoryDTO(
        Long idTypeCategory,

        @NotBlank(message = "El nombre del tipo de categoria es obligatorio")
        String nameTypeCategory,

        String description
) {
}
