package com.vria.transfer.service;

import com.vria.notifications.enums.NotificationAction;
import com.vria.notifications.enums.NotificationModule;
import com.vria.notifications.event.ActivityEvent;
import com.vria.notifications.support.Money;
import com.vria.transfer.dto.TransferDTO;
import com.vria.transfer.model.Transfer;
import com.vria.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {

    private final TransferRepository transferRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional(readOnly = true)
    public List<TransferDTO> findAll() {
        return transferRepository.findAll().stream().map(this::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TransferDTO findById(Long id) {
        return toDTO(findEntity(id));
    }

    public TransferDTO create(TransferDTO dto) {
        Transfer entity = Transfer.builder()
                .amount(dto.amount())
                .destination(dto.destination())
                .dateTransfer(dto.dateTransfer())
                .nameClient(dto.nameClient())
                .bank(dto.bank())
                .build();
        Transfer saved = transferRepository.save(entity);
        publish(NotificationAction.CREATED, saved, "Se registró una transferencia de " + Money.format(saved.getAmount())
                + " de " + saved.getNameClient() + " por " + saved.getBank() + ".");
        return toDTO(saved);
    }

    public void delete(Long id) {
        Transfer entity = findEntity(id);
        transferRepository.delete(entity);
        publish(NotificationAction.DELETED, entity, "Se eliminó la transferencia de " + Money.format(entity.getAmount())
                + " de " + entity.getNameClient() + ".");
    }

    private void publish(NotificationAction action, Transfer transfer, String summary) {
        eventPublisher.publishEvent(new ActivityEvent(
                NotificationModule.TRANSFER, action, transfer.getIdTransfer(), transfer.getNameClient(), summary));
    }

    private Transfer findEntity(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transferencia no encontrada con id: " + id));
    }

    private TransferDTO toDTO(Transfer entity) {
        return new TransferDTO(
                entity.getIdTransfer(),
                entity.getAmount(),
                entity.getDestination(),
                entity.getDateTransfer(),
                entity.getNameClient(),
                entity.getBank()
        );
    }
}
