package com.vria.users.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Roles disponibles dentro del sistema VRIA.
 */
@Getter
@RequiredArgsConstructor
public enum TypeRole {
    ADMIN("administrador"),
    WAREHOUSE_KEEPER("encargado de bodega"),
    COMMERCIAL_ADVISOR("asesor comercial");

    private final String label;
}
