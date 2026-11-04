package com.vria.products.mapper;

import com.vria.products.dto.CategoryDTO;
import com.vria.products.model.Category;
import org.springframework.stereotype.Component;

/**
 * Conversiones entre la entidad Category y su DTO.
 */
@Component
public class CategoryMapper {

    public CategoryDTO toDTO(Category category) {
        return new CategoryDTO(
                category.getIdCategory(),
                category.getNameCategory()
        );
    }

    public Category toEntity(CategoryDTO dto) {
        return Category.builder()
                .nameCategory(dto.nameCategory())
                .build();
    }

    public void updateEntity(Category category, CategoryDTO dto) {
        category.setNameCategory(dto.nameCategory());
    }
}
