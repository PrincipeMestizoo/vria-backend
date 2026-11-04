package com.vria.delivery.mapper;

import com.vria.delivery.dto.DeliveryDTO;
import com.vria.delivery.model.Delivery;
import com.vria.users.model.User;
import org.springframework.stereotype.Component;

/**
 * Conversiones entre la entidad Delivery y su DTO.
 */
@Component
public class DeliveryMapper {

    public DeliveryDTO toDTO(Delivery delivery) {
        User user = delivery.getUser();
        return new DeliveryDTO(
                delivery.getIdDelivery(),
                delivery.getNameClient(),
                delivery.getNameDelivery(),
                delivery.getAddress(),
                delivery.getPayMode(),
                delivery.getDateDelivery(),
                delivery.getState(),
                user.getIdUser(),
                user.fullName(),
                user.getRole()
        );
    }

    // Los campos de solo lectura del DTO (usuario) se ignoran: el autor es quien inicia el despacho
    public Delivery toEntity(DeliveryDTO dto, User initiator) {
        return Delivery.builder()
                .nameClient(dto.nameClient())
                .nameDelivery(dto.nameDelivery())
                .address(dto.address())
                .payMode(dto.payMode())
                .dateDelivery(dto.dateDelivery())
                .state(dto.state())
                .user(initiator)
                .build();
    }
}
