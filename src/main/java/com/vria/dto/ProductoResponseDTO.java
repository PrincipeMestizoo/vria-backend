package com.vria.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductoResponseDTO(
        Long id,
        String codigoInterno,
        String nombre,
        String categoria,
        String proveedor,
        Integer cantidadBultos,
        Integer cantidadStock,
        BigDecimal precioVenta,
        Integer umbralStockCriticoPersonalizado,
        boolean stockCritico,
        LocalDateTime fechaRecepcion
) {
}
