package com.vria.products.repository;

import com.vria.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory_IdCategory(Long idCategory);
}
