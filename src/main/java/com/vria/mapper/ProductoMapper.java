package com.vria.mapper;

import com.vria.dto.ProductoRequestDTO;
import com.vria.dto.ProductoResponseDTO;
import com.vria.model.Producto;
import org.springframework.stereotype.Component;

@Component
public class ProductoMapper {

    public Producto toEntity(ProductoRequestDTO dto) {
        return Producto.builder()
                .codigoInterno(dto.codigoInterno())
                .nombre(dto.nombre())
                .categoria(dto.categoria())
                .proveedor(dto.proveedor())
                .cantidadBultos(dto.cantidadBultos())
                .cantidadStock(dto.cantidadStock())
                .precioVenta(dto.precioVenta())
                .umbralStockCriticoPersonalizado(dto.umbralStockCriticoPersonalizado())
                .build();
    }

    /** Copia los campos editables del DTO sobre una entidad ya existente (update). */
    public void actualizarEntidad(Producto producto, ProductoRequestDTO dto) {
        producto.setNombre(dto.nombre());
        producto.setCategoria(dto.categoria());
        producto.setProveedor(dto.proveedor());
        producto.setCantidadBultos(dto.cantidadBultos());
        producto.setCantidadStock(dto.cantidadStock());
        producto.setPrecioVenta(dto.precioVenta());
        producto.setUmbralStockCriticoPersonalizado(dto.umbralStockCriticoPersonalizado());
    }

    public ProductoResponseDTO toResponseDTO(Producto producto) {
        return new ProductoResponseDTO(
                producto.getId(),
                producto.getCodigoInterno(),
                producto.getNombre(),
                producto.getCategoria(),
                producto.getProveedor(),
                producto.getCantidadBultos(),
                producto.getCantidadStock(),
                producto.getPrecioVenta(),
                producto.getUmbralStockCriticoPersonalizado(),
                producto.esStockCritico(),
                producto.getFechaRecepcion()
        );
    }
}
