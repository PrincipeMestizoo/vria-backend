package com.vria.products.service;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityEvent;
import com.vria.notifications.support.Money;
import com.vria.notifications.support.Quantity;
import com.vria.products.dto.ProductRequestDTO;
import com.vria.products.dto.ProductResponseDTO;
import com.vria.products.model.Category;
import com.vria.products.model.Product;
import com.vria.products.repository.CategoryRepository;
import com.vria.products.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ApplicationEventPublisher eventPublisher;

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
        Product saved = productRepository.save(product);
        publish(NotificationAction.CREATED, saved,
                "Se agregó " + saved.getNameProduct() + " al inventario con " + Quantity.units(saved.getStock()) + ".");
        return toResponse(saved);
    }

    public ProductResponseDTO update(Long id, ProductRequestDTO dto) {
        Product product = findEntity(id);
        String changes = describeChanges(product, dto);
        product.setNameProduct(dto.nameProduct());
        product.setCategory(findCategory(dto.idCategory()));
        product.setStock(dto.stock());
        product.setPrice(dto.price());
        product.setReference(dto.reference());
        product.setDescription(dto.description());
        product.setPhoto(dto.photo());
        Product saved = productRepository.save(product);
        publish(NotificationAction.UPDATED, saved, changes);
        return toResponse(saved);
    }

    public ProductResponseDTO reduceProduct(int quantity, Long id) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a reducir debe ser mayor a 0");
        }
        Product product = findEntity(id);
        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Stock insuficiente para el producto " + product.getNameProduct()
                    + ": disponible " + product.getStock() + ", solicitado " + quantity);
        }
        product.setStock(product.getStock() - quantity);
        Product saved = productRepository.save(product);
        publish(NotificationAction.STOCK_REDUCED, saved, (quantity == 1 ? "Salió " : "Salieron ") + Quantity.units(quantity)
                + " de " + saved.getNameProduct() + ". Quedan " + saved.getStock() + ".");
        return toResponse(saved);
    }

    public void delete(Long id) {
        Product product = findEntity(id);
        productRepository.delete(product);
        publish(NotificationAction.DELETED, product, "Se eliminó " + product.getNameProduct() + " del inventario.");
    }

    private void publish(NotificationAction action, Product product, String summary) {
        eventPublisher.publishEvent(new ActivityEvent(
                NotificationModule.PRODUCT, action, product.getIdProduct(), product.getNameProduct(), summary));
    }

    // Describe en una frase solo los cambios relevantes para inventario y ventas
    private String describeChanges(Product current, ProductRequestDTO dto) {
        List<String> changes = new ArrayList<>();
        if (!current.getNameProduct().equals(dto.nameProduct())) {
            changes.add("antes se llamaba " + current.getNameProduct());
        }
        if (current.getStock() != dto.stock()) {
            changes.add("ahora hay " + Quantity.units(dto.stock()) + " (antes " + current.getStock() + ")");
        }
        if (Float.compare(current.getPrice(), dto.price()) != 0) {
            changes.add("el precio pasó de " + Money.format(current.getPrice()) + " a " + Money.format(dto.price()));
        }
        if (changes.isEmpty()) {
            return "Se actualizó la información de " + dto.nameProduct() + ".";
        }
        return "Se actualizó " + dto.nameProduct() + ": " + joinNatural(changes) + ".";
    }

    private static String joinNatural(List<String> parts) {
        if (parts.size() == 1) {
            return parts.getFirst();
        }
        return String.join(", ", parts.subList(0, parts.size() - 1)) + " y " + parts.getLast();
    }

    private Product findEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con id: " + id));
    }

    private Category findCategory(Long id) {
        if (id == null) {
            return null;
        }
        return categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Categoria no encontrada con id: " + id));
    }

    private ProductResponseDTO toResponse(Product product) {
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
}
