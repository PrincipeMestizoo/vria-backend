package com.vria.delivery.dto;

import com.vria.delivery.enums.StateDelivery;
import com.vria.transfer.enums.PayMode;
import com.vria.users.enums.TypeRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record DeliveryDTO(
        Long idDelivery,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String nameClient,

        @NotBlank(message = "El nombre del repartidor es obligatorio")
        String nameDelivery,

        @NotBlank(message = "La direccion es obligatoria")
        String address,

        @NotNull(message = "El modo de pago es obligatorio")
        PayMode payMode,

        @NotNull(message = "La fecha de entrega es obligatoria")
        LocalDateTime dateDelivery,

        @NotNull(message = "El estado de la entrega es obligatorio")
        StateDelivery state,

        // Solo lectura
        Long idUser,
        String nameUser,
        TypeRole roleUser
) {
}
