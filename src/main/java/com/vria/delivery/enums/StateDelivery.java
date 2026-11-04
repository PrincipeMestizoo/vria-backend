package com.vria.delivery.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StateDelivery {
    PREPARING("En preparación"),
    READY("Lista"),
    SHIPPED("Enviada"),
    DELIVERED("Entregada");

    private final String label;
}
