package com.vria.delivery.model;

import com.vria.transfer.enums.PayMode;
import com.vria.delivery.enums.StateDelivery;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "deliveries")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delivery")
    private Long idDelivery;

    @Column(name = "name_client", nullable = false, length = 150)
    private String nameClient;

    @Column(name = "name_delivery", nullable = false, length = 150)
    private String nameDelivery;

    @Column(name = "address", nullable = false, length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "pay_mode", nullable = false, length = 20)
    private PayMode payMode;

    @Column(name = "date_delivery", nullable = false)
    private LocalDateTime dateDelivery;

    @Enumerated(EnumType.STRING)
    @Column(name = "state", nullable = false, length = 20)
    private StateDelivery state;
}
