package com.vria.products.repository;

import com.vria.products.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory_IdCategory(Long idCategory);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Product p SET p.category = null WHERE p.category.idCategory = :idCategory")
    int clearCategory(@Param("idCategory") Long idCategory);
}
