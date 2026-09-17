package com.vria.products.service;

import com.vria.products.dto.TypeCategoryDTO;
import com.vria.products.model.TypeCategory;
import com.vria.products.repository.TypeCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TypeCategoryService {

    private final TypeCategoryRepository typeCategoryRepository;

    @Transactional(readOnly = true)
    public List<TypeCategoryDTO> findAll() {
        return typeCategoryRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TypeCategoryDTO findById(Long id) {
        return toDTO(findEntity(id));
    }

    public TypeCategoryDTO create(TypeCategoryDTO dto) {
        TypeCategory entity = TypeCategory.builder()
                .nameTypeCategory(dto.nameTypeCategory())
                .description(dto.description())
                .build();
        return toDTO(typeCategoryRepository.save(entity));
    }

    public TypeCategoryDTO update(Long id, TypeCategoryDTO dto) {
        TypeCategory entity = findEntity(id);
        entity.setNameTypeCategory(dto.nameTypeCategory());
        entity.setDescription(dto.description());
        return toDTO(typeCategoryRepository.save(entity));
    }

    public void delete(Long id) {
        typeCategoryRepository.delete(findEntity(id));
    }

    private TypeCategory findEntity(Long id) {
        return typeCategoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Tipo de categoria no encontrado con id: " + id));
    }

    private TypeCategoryDTO toDTO(TypeCategory entity) {
        return new TypeCategoryDTO(entity.getIdTypeCategory(), entity.getNameTypeCategory(), entity.getDescription());
    }
}
