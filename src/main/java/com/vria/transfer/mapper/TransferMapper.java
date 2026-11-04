package com.vria.transfer.mapper;

import com.vria.transfer.dto.TransferDTO;
import com.vria.transfer.model.Transfer;
import org.springframework.stereotype.Component;

/**
 * Conversiones entre la entidad Transfer y su DTO.
 */
@Component
public class TransferMapper {

    public TransferDTO toDTO(Transfer transfer) {
        return new TransferDTO(
                transfer.getIdTransfer(),
                transfer.getAmount(),
                transfer.getDestination(),
                transfer.getDateTransfer(),
                transfer.getNameClient(),
                transfer.getBank()
        );
    }

    public Transfer toEntity(TransferDTO dto) {
        return Transfer.builder()
                .amount(dto.amount())
                .destination(dto.destination())
                .dateTransfer(dto.dateTransfer())
                .nameClient(dto.nameClient())
                .bank(dto.bank())
                .build();
    }
}
