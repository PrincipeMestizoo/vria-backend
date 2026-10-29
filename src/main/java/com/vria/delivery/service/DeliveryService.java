package com.vria.delivery.service;

import com.vria.delivery.dto.DeliveryDTO;
import com.vria.delivery.enums.StateDelivery;
import com.vria.delivery.model.Delivery;
import com.vria.delivery.repository.DeliveryRepository;
import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityEvent;
import com.vria.users.model.User;
import com.vria.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<DeliveryDTO> findAll() {
        return deliveryRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public DeliveryDTO findById(Long id) {
        return toDTO(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<DeliveryDTO> findByState(StateDelivery state) {
        return deliveryRepository.findByState(state).stream().map(this::toDTO).toList();
    }

    public DeliveryDTO create(DeliveryDTO dto, User initiator) {
        Delivery entity = Delivery.builder()
                .nameClient(dto.nameClient())
                .nameDelivery(dto.nameDelivery())
                .address(dto.address())
                .payMode(dto.payMode())
                .dateDelivery(dto.dateDelivery())
                .state(dto.state())
                .user(userRepository.getReferenceById(initiator.getIdUser()))
                .build();
        Delivery saved = deliveryRepository.save(entity);
        publish(NotificationAction.CREATED, saved, "Se creó un domicilio para " + saved.getNameClient()
                + " (" + saved.getState().getLabel().toLowerCase() + ").");
        return toDTO(saved);
    }

    public DeliveryDTO updateState(Long id, StateDelivery state) {
        Delivery entity = findEntity(id);
        StateDelivery previous = entity.getState();
        entity.setState(state);
        Delivery saved = deliveryRepository.save(entity);
        publish(NotificationAction.STATE_CHANGED, saved, "El domicilio de " + saved.getNameClient() + " pasó de "
                + previous.getLabel().toLowerCase() + " a " + state.getLabel().toLowerCase() + ".");
        return toDTO(saved);
    }

    public void delete(Long id) {
        Delivery entity = findEntity(id);
        deliveryRepository.delete(entity);
        publish(NotificationAction.DELETED, entity, "Se eliminó el domicilio de " + entity.getNameClient() + ".");
    }

    private void publish(NotificationAction action, Delivery delivery, String summary) {
        eventPublisher.publishEvent(new ActivityEvent(
                NotificationModule.DELIVERY, action, delivery.getIdDelivery(), delivery.getNameClient(), summary));
    }

    private Delivery findEntity(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrega no encontrada con id: " + id));
    }

    private DeliveryDTO toDTO(Delivery entity) {
        return new DeliveryDTO(
                entity.getIdDelivery(),
                entity.getNameClient(),
                entity.getNameDelivery(),
                entity.getAddress(),
                entity.getPayMode(),
                entity.getDateDelivery(),
                entity.getState(),
                entity.getUser().getIdUser(),
                entity.getUser().getName() + " " + entity.getUser().getLastName(),
                entity.getUser().getRole()
        );
    }
}
