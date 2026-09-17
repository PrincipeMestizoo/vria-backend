package com.vria.products.controller;

import com.vria.products.dto.TypeCategoryDTO;
import com.vria.products.service.TypeCategoryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/type-categories")
@RequiredArgsConstructor
@Tag(name = "Tipos de categoria", description = "Catalogo de tipos de categoria de producto")
@PreAuthorize("hasAnyRole('ADMIN', 'WAREHOUSE_KEEPER')")
public class TypeCategoryController {

    private final TypeCategoryService typeCategoryService;

    @GetMapping
    public ResponseEntity<List<TypeCategoryDTO>> findAll() {
        return ResponseEntity.ok(typeCategoryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeCategoryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(typeCategoryService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TypeCategoryDTO> create(@Valid @RequestBody TypeCategoryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(typeCategoryService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TypeCategoryDTO> update(@PathVariable Long id, @Valid @RequestBody TypeCategoryDTO dto) {
        return ResponseEntity.ok(typeCategoryService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        typeCategoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
