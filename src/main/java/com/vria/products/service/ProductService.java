package com.vria.products.service;

import com.vria.products.activity.ProductActivityNotifier;
import com.vria.products.dto.ProductRequestDTO;
import com.vria.products.dto.ProductResponseDTO;
import com.vria.products.mapper.ProductMapper;
import com.vria.products.model.Category;
import com.vria.products.model.Product;
import com.vria.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final ProductMapper productMapper;
    private final ProductActivityNotifier activityNotifier;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return productMapper.toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByCategory(Long idCategory) {
        return productRepository.findByCategory_IdCategory(idCategory).stream().map(productMapper::toResponse).toList();
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = categoryService.findOptionalEntity(dto.idCategory());
        Product saved = productRepository.save(productMapper.toEntity(dto, category));
        activityNotifier.created(saved);
        return productMapper.toResponse(saved);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = findEntity(id);
        // Copia del estado anterior para describir los cambios en la notificacion
        Product previous = product.toBuilder().build();
        productMapper.updateEntity(product, dto, categoryService.findOptionalEntity(dto.idCategory()));
        Product saved = productRepository.save(product);
        activityNotifier.updated(previous, saved);
        return productMapper.toResponse(saved);
    }

    public ProductResponseDTO reduceStock(Long id, int quantity) {
        Product product = findEntity(id);
        product.reduceStock(quantity);
        Product saved = productRepository.save(product);
        activityNotifier.stockReduced(saved, quantity);
        return productMapper.toResponse(saved);
    }

    public void delete(Long id) {
        Product product = findEntity(id);
        productRepository.delete(product);
        activityNotifier.deleted(product);
    }

    private Product findEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
    }
}
