package com.vria.products.service;

import com.vria.products.activity.CategoryActivityNotifier;
import com.vria.products.dto.CategoryDTO;
import com.vria.products.mapper.CategoryMapper;
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
    private final CategoryMapper categoryMapper;
    private final CategoryActivityNotifier activityNotifier;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryRepository.findAll().stream().map(categoryMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        return categoryMapper.toDTO(findEntity(id));
    }

    /**
     * Busca la categoria por id; un id nulo significa "sin categoria".
     */
    @Transactional(readOnly = true)
    public Category findOptionalEntity(Long id) {
        return id == null ? null : findEntity(id);
    }

    public CategoryDTO create(CategoryDTO dto) {
        Category saved = categoryRepository.save(categoryMapper.toEntity(dto));
        activityNotifier.created(saved);
        return categoryMapper.toDTO(saved);
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category category = findEntity(id);
        String previousName = category.getNameCategory();
        categoryMapper.updateEntity(category, dto);
        Category saved = categoryRepository.save(category);
        activityNotifier.updated(previousName, saved);
        return categoryMapper.toDTO(saved);
    }

    public void delete(Long id) {
        Category category = findEntity(id);
        // Desvincula los productos antes de borrar para no violar la llave foranea
        productRepository.clearCategory(id);
        categoryRepository.delete(category);
        activityNotifier.deleted(category);
    }

    private Category findEntity(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id: " + id));
    }
}
