package com.vria.transfer.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Registro de una transferencia bancaria recibida como comprobante de pago.
 */
@Entity
@Table(name = "transfers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transfer")
    private Long idTransfer;

    @Column(name = "amount", nullable = false)
    private float amount;

    @Column(name = "destination", nullable = false, length = 150)
    private String destination;

    @Column(name = "date_transfer", nullable = false)
    private LocalDateTime dateTransfer;

    @Column(name = "name_client", nullable = false, length = 150)
    private String nameClient;

    @Column(name = "bank", nullable = false, length = 100)
    private String bank;
}
