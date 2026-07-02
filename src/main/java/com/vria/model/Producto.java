package com.vria.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, unique = true, length = 50)
    private String codigoInterno;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String nombre;

    @NotBlank
    @Column(nullable = false, length = 80)
    private String categoria;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String proveedor;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer cantidadBultos;

    @NotNull
    @Min(0)
    @Column(nullable = false)
    private Integer cantidadStock;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal precioVenta;

    /** Umbral hibrido (3.3): si es null, se usa el umbral por defecto de la empresa. */
    @Column
    private Integer umbralStockCriticoPersonalizado;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fechaRecepcion;

    @PrePersist
    void alCrear() {
        if (fechaRecepcion == null) {
            fechaRecepcion = LocalDateTime.now();
        }
    }

    /** Umbral por defecto aplicado cuando el producto no tiene uno personalizado. */
    public static final int UMBRAL_POR_DEFECTO_EMPRESA = 5;

    public boolean esStockCritico() {
        int umbralAplicable = (umbralStockCriticoPersonalizado != null)
                ? umbralStockCriticoPersonalizado
                : UMBRAL_POR_DEFECTO_EMPRESA;
        return cantidadStock <= umbralAplicable;
    }
}
