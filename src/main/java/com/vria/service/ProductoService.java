package com.vria.service;

import com.vria.dto.ProductoRequestDTO;
import com.vria.dto.ProductoResponseDTO;

import java.util.List;

public interface ProductoService {

    ProductoResponseDTO crear(ProductoRequestDTO dto);

    ProductoResponseDTO obtenerPorId(Long id);

    List<ProductoResponseDTO> listarTodos();

    ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto);

    void eliminar(Long id);

    List<ProductoResponseDTO> listarEnStockCritico();
}
