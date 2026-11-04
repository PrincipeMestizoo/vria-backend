package com.vria.products.dto;

import jakarta.validation.constraints.*;

public record ProductRequestDTO(

        @NotBlank(message = "El nombre del producto es obligatorio")
        String nameProduct,

        // Opcional: un producto puede quedar sin categoria
        Long idCategory,

        @PositiveOrZero(message = "El stock no puede ser negativo")
        int stock,

        @Positive(message = "El precio debe ser mayor a cero")
        float price,

        String reference,

        String description,

        String photo
) {
}
