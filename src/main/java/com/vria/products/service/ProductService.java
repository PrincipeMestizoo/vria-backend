package com.vria.products.service;

import com.vria.products.dto.ProductRequestDTO;
import com.vria.products.dto.ProductResponseDTO;
import com.vria.products.model.Category;
import com.vria.products.model.Product;
import com.vria.products.repository.CategoryRepository;
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
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findAll() {
        return productRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public ProductResponseDTO findById(Long id) {
        return toResponse(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<ProductResponseDTO> findByCategory(Long idCategory) {
        return productRepository.findByCategory_IdCategory(idCategory).stream().map(this::toResponse).toList();
    }

    public ProductResponseDTO create(ProductRequestDTO dto) {
        Category category = findCategory(dto.idCategory());
        Product product = Product.builder()
                .nameProduct(dto.nameProduct())
                .category(category)
                .stock(dto.stock())
                .price(dto.price())
                .reference(dto.reference())
                .description(dto.description())
                .photo(dto.photo())
                .build();
        return toResponse(productRepository.save(product));
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = findEntity(id);
        product.setNameProduct(dto.nameProduct());
        product.setCategory(findCategory(dto.idCategory()));
        product.setStock(dto.stock());
        product.setPrice(dto.price());
        product.setReference(dto.reference());
        product.setDescription(dto.description());
        product.setPhoto(dto.photo());
        return toResponse(productRepository.save(product));
    }

    public void delete(Long id) {
        productRepository.delete(findEntity(id));
    }

    private Product findEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
    }

    private Category findCategory(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id: " + id));
    }

    private ProductResponseDTO toResponse(Product product) {
        return new ProductResponseDTO(
                product.getIdProduct(),
                product.getNameProduct(),
                product.getCategory().getIdCategory(),
                product.getCategory().getNameCategory(),
                product.getStock(),
                product.getPrice(),
                product.getReference(),
                product.getDescription(),
                product.getPhoto()
        );
    }
}
