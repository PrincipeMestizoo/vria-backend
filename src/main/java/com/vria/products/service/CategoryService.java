package com.vria.products.service;

import com.vria.products.dto.CategoryDTO;
import com.vria.products.model.Category;
import com.vria.products.repository.CategoryRepository;
import com.vria.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return toDTO(findEntity(id));
    }

    public CategoryDTO create(CategoryDTO dto) {
        Category entity = Category.builder()
                .nameCategory(dto.nameCategory())
                .build();
        return toDTO(categoryRepository.save(entity));
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category entity = findEntity(id);
        entity.setNameCategory(dto.nameCategory());
        return toDTO(categoryRepository.save(entity));
    }

    public void delete(Long id) {
        Category entity = findEntity(id);
        // Desvincula los productos antes de borrar para no violar la llave foranea
        productRepository.clearCategory(id);
        categoryRepository.delete(entity);
    }

    private Category findEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id: " + id));
    }

    private CategoryDTO toDTO(Category entity) {
        return new CategoryDTO(
                entity.getIdCategory(),
                entity.getNameCategory()
        );
    }
}
