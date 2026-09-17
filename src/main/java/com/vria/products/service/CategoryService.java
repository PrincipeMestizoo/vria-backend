package com.vria.products.service;

import com.vria.products.dto.CategoryDTO;
import com.vria.products.model.Category;
import com.vria.products.model.TypeCategory;
import com.vria.products.repository.CategoryRepository;
import com.vria.products.repository.TypeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final TypeCategoryRepository typeCategoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return toDTO(findEntity(id));
    }

    public CategoryDTO create(CategoryDTO dto) {
        TypeCategory typeCategory = findTypeCategory(dto.idTypeCategory());
        Category entity = Category.builder()
                .nameCategory(dto.nameCategory())
                .typeCategory(typeCategory)
                .build();
        return toDTO(categoryRepository.save(entity));
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category entity = findEntity(id);
        entity.setNameCategory(dto.nameCategory());
        entity.setTypeCategory(findTypeCategory(dto.idTypeCategory()));
        return toDTO(categoryRepository.save(entity));
    }

    public void delete(Long id) {
        categoryRepository.delete(findEntity(id));
    }

    private Category findEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id: " + id));
    }

    private TypeCategory findTypeCategory(Long id) {
        return typeCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de categoria no encontrado con id: " + id));
    }

    private CategoryDTO toDTO(Category entity) {
        return new CategoryDTO(
                entity.getIdCategory(),
                entity.getNameCategory(),
                entity.getTypeCategory().getIdTypeCategory(),
                entity.getTypeCategory().getNameTypeCategory()
        );
    }
}
