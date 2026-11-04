package com.vria.products.mapper;

import com.vria.products.dto.ProductRequestDTO;
import com.vria.products.dto.ProductResponseDTO;
import com.vria.products.model.Category;
import com.vria.products.model.Product;
import org.springframework.stereotype.Component;

/**
 * Conversiones entre la entidad Product y sus DTOs.
 * La categoria llega ya resuelta (o null): el mapper no consulta repositorios.
 */
@Component
public class ProductMapper {

    public ProductResponseDTO toResponse(Product product) {
        Category category = product.getCategory();
        return new ProductResponseDTO(
                product.getIdProduct(),
                product.getNameProduct(),
                category != null ? category.getIdCategory() : null,
                category != null ? category.getNameCategory() : null,
                product.getStock(),
                product.getPrice(),
                product.getReference(),
                product.getDescription(),
                product.getPhoto()
        );
    }

    public Product toEntity(ProductRequestDTO dto, Category category) {
        return Product.builder()
                .nameProduct(dto.nameProduct())
                .category(category)
                .stock(dto.stock())
                .price(dto.price())
                .reference(dto.reference())
                .description(dto.description())
                .photo(dto.photo())
                .build();
    }

    public void updateEntity(Product product, ProductRequestDTO dto, Category category) {
        product.setNameProduct(dto.nameProduct());
        product.setCategory(category);
        product.setStock(dto.stock());
        product.setPrice(dto.price());
        product.setReference(dto.reference());
        product.setDescription(dto.description());
        product.setPhoto(dto.photo());
    }
}
