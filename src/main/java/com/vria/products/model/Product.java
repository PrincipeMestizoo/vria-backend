package com.vria.products.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long idProduct;

    @Column(name = "name_product", nullable = false, length = 150)
    private String nameProduct;

    // Opcional: al eliminar una categoria sus productos quedan sin categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category")
    private Category category;

    @Column(name = "stock", nullable = false)
    private int stock;

    @Column(name = "price", nullable = false)
    private float price;

    @Column(name = "reference", length = 100)
    private String reference;

    @Column(name = "description", length = 500)
    private String description;

    // URL de la imagen almacenada en Cloudinary
    @Column(name = "photo")
    private String photo;

    /**
     * Descuenta unidades del inventario validando que haya stock suficiente.
     */
    public void reduceStock(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("La cantidad a reducir debe ser mayor a 0");
        }
        if (quantity > stock) {
            throw new IllegalArgumentException("Stock insuficiente para el producto " + nameProduct
                    + ": disponible " + stock + ", solicitado " + quantity);
        }
        stock -= quantity;
    }
}
