package com.vria.transfer.service;

import com.vria.transfer.activity.TransferActivityNotifier;
import com.vria.transfer.dto.TransferDTO;
import com.vria.transfer.mapper.TransferMapper;
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
    private final TransferMapper transferMapper;
    private final TransferActivityNotifier activityNotifier;

    @Transactional(readOnly = true)
    public List<TransferDTO> findAll() {
        return transferRepository.findAll().stream().map(transferMapper::toDTO).toList();
    }

    @Transactional(readOnly = true)
    public TransferDTO findById(Long id) {
        return transferMapper.toDTO(findEntity(id));
    }

    public TransferDTO create(TransferDTO dto) {
        Transfer saved = transferRepository.save(transferMapper.toEntity(dto));
        activityNotifier.created(saved);
        return transferMapper.toDTO(saved);
    }

    public void delete(Long id) {
        Transfer transfer = findEntity(id);
        transferRepository.delete(transfer);
        activityNotifier.deleted(transfer);
    }

    private Transfer findEntity(Long id) {
        return transferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transferencia no encontrada con id: " + id));
    }
}
