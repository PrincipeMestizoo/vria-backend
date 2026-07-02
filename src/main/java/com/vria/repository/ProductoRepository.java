package com.vria.repository;

import com.vria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, Long> {

    Optional<Producto> findByCodigoInterno(String codigoInterno);

    boolean existsByCodigoInterno(String codigoInterno);
}
