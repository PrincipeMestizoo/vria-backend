package com.vria.products.service;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityEvent;
import com.vria.products.dto.CategoryDTO;
import com.vria.products.model.Category;
import com.vria.products.repository.CategoryRepository;
import com.vria.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

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
        Category saved = categoryRepository.save(entity);
        publish(NotificationAction.CREATED, saved, "Se creó la categoría " + saved.getNameCategory() + ".");
        return toDTO(saved);
    }

    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category entity = findEntity(id);
        String previousName = entity.getNameCategory();
        entity.setNameCategory(dto.nameCategory());
        Category saved = categoryRepository.save(entity);
        publish(NotificationAction.UPDATED, saved, previousName.equals(saved.getNameCategory())
                ? "Se actualizó la categoría " + saved.getNameCategory() + "."
                : "La categoría " + previousName + " ahora se llama " + saved.getNameCategory() + ".");
        return toDTO(saved);
    }

    public void delete(Long id) {
        Category entity = findEntity(id);
        // Desvincula los productos antes de borrar para no violar la llave foranea
        productRepository.clearCategory(id);
        categoryRepository.delete(entity);
        publish(NotificationAction.DELETED, entity, "Se eliminó la categoría " + entity.getNameCategory() + ".");
    }

    private void publish(NotificationAction action, Category category, String summary) {
        eventPublisher.publishEvent(new ActivityEvent(
                NotificationModule.CATEGORY, action, category.getIdCategory(), category.getNameCategory(), summary));
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
