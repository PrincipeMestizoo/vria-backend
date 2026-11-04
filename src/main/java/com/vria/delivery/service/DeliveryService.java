package com.vria.delivery.service;

import com.vria.delivery.activity.DeliveryActivityNotifier;
import com.vria.delivery.dto.DeliveryDTO;
import com.vria.delivery.enums.StateDelivery;
import com.vria.delivery.mapper.DeliveryMapper;
import com.vria.delivery.model.Delivery;
import com.vria.delivery.repository.DeliveryRepository;
import com.vria.users.model.User;
import com.vria.users.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final UserRepository userRepository;
    private final DeliveryMapper deliveryMapper;
    private final DeliveryActivityNotifier activityNotifier;

    @Transactional(readOnly = true)
    public List<DeliveryDTO> findAll() {
        return deliveryRepository.findAll().stream().map(deliveryMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public DeliveryDTO findById(Long id) {
        return deliveryMapper.toDTO(findEntity(id));
    }

    @Transactional(readOnly = true)
    public List<DeliveryDTO> findByState(StateDelivery state) {
        return deliveryRepository.findByState(state).stream().map(deliveryMapper::toDTO).toList();
    }

    public DeliveryDTO create(DeliveryDTO dto, User initiator) {
        User user = userRepository.getReferenceById(initiator.getIdUser());
        Delivery saved = deliveryRepository.save(deliveryMapper.toEntity(dto, user));
        activityNotifier.created(saved);
        return deliveryMapper.toDTO(saved);
    }

    public DeliveryDTO updateState(Long id, StateDelivery state) {
        Delivery delivery = findEntity(id);
        StateDelivery previous = delivery.getState();
        delivery.setState(state);
        Delivery saved = deliveryRepository.save(delivery);
        activityNotifier.stateChanged(previous, saved);
        return deliveryMapper.toDTO(saved);
    }

    public void delete(Long id) {
        Delivery delivery = findEntity(id);
        deliveryRepository.delete(delivery);
        activityNotifier.deleted(delivery);
    }

    private Delivery findEntity(Long id) {
        return deliveryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Entrega no encontrada con id: " + id));
    }
}
