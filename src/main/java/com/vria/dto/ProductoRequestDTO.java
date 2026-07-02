package com.vria.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO de entrada. Desacopla el contrato de la API del modelo de persistencia
 * (evita exponer campos como fechaRecepcion, que el sistema controla).
 */
public record ProductoRequestDTO(

        @NotBlank(message = "El codigo interno es obligatorio")
        String codigoInterno,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "La categoria es obligatoria")
        String categoria,

        @NotBlank(message = "El proveedor es obligatorio")
        String proveedor,

        @NotNull @Min(value = 0, message = "La cantidad de bultos no puede ser negativa")
        Integer cantidadBultos,

        @NotNull @Min(value = 0, message = "La cantidad en stock no puede ser negativa")
        Integer cantidadStock,

        @NotNull @DecimalMin(value = "0.0", message = "El precio de venta no puede ser negativo")
        BigDecimal precioVenta,

        Integer umbralStockCriticoPersonalizado
) {
}
