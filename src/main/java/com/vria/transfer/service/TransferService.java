package com.vria.transfer.service;

import com.vria.transfer.dto.TransferDTO;
import com.vria.transfer.model.Transfer;
import com.vria.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TransferService {

    private final TransferRepository transferRepository;

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
        return toDTO(transferRepository.save(entity));
    }

    public void delete(Long id) {
        transferRepository.delete(findEntity(id));
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
