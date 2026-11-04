package com.vria.transfer.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;

public record TransferDTO(
        Long idTransfer,

        @Positive(message = "El monto debe ser mayor a cero")
        float amount,

        @NotBlank(message = "El destino es obligatorio")
        String destination,

        @NotNull(message = "La fecha de la transferencia es obligatoria")
        LocalDateTime dateTransfer,

        @NotBlank(message = "El nombre del cliente es obligatorio")
        String nameClient,

        @NotBlank(message = "El banco es obligatorio")
        String bank
) {
}
