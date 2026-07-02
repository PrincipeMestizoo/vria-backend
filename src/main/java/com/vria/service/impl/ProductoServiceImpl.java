package com.vria.service.impl;

import com.vria.dto.ProductoRequestDTO;
import com.vria.dto.ProductoResponseDTO;
import com.vria.exception.CodigoDuplicadoException;
import com.vria.exception.ResourceNotFoundException;
import com.vria.mapper.ProductoMapper;
import com.vria.model.Producto;
import com.vria.repository.ProductoRepository;
import com.vria.service.ProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private final ProductoMapper productoMapper;

    @Override
    public ProductoResponseDTO crear(ProductoRequestDTO dto) {
        if (productoRepository.existsByCodigoInterno(dto.codigoInterno())) {
            throw new CodigoDuplicadoException(
                    "Ya existe un producto con el codigo interno: " + dto.codigoInterno());
        }
        Producto producto = productoMapper.toEntity(dto);
        Producto guardado = productoRepository.save(producto);
        return productoMapper.toResponseDTO(guardado);
    }

    @Override
    @Transactional(readOnly = true)
    public ProductoResponseDTO obtenerPorId(Long id) {
        return productoMapper.toResponseDTO(buscarOFallar(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarTodos() {
        return productoRepository.findAll().stream()
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    @Override
    public ProductoResponseDTO actualizar(Long id, ProductoRequestDTO dto) {
        Producto producto = buscarOFallar(id);
        productoMapper.actualizarEntidad(producto, dto);
        return productoMapper.toResponseDTO(productoRepository.save(producto));
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Producto no encontrado con id: " + id);
        }
        productoRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductoResponseDTO> listarEnStockCritico() {
        return productoRepository.findAll().stream()
                .filter(Producto::esStockCritico)
                .map(productoMapper::toResponseDTO)
                .toList();
    }

    private Producto buscarOFallar(Long id) {
        return productoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con id: " + id));
    }
}
