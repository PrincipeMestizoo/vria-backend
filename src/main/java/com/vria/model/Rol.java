package com.vria.model;

/**
 * Roles definidos en el Informe VRIA (seccion 2.1 - Perfil de encuestados).
 * Solo ADMINISTRADOR y BODEGUERO tienen permiso de escritura sobre el CRUD
 * de productos; ASESOR se deja modelado para futuros modulos (ventas).
 */
public enum Rol {
    ADMINISTRADOR,
    BODEGUERO,
    ASESOR
}
