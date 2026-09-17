package com.vria.products.dto;

public record ProductResponseDTO(
        Long idProduct,
        String nameProduct,
        Long idCategory,
        String nameCategory,
        int stock,
        float price,
        String reference,
        String description,
        String photo
) {
}
