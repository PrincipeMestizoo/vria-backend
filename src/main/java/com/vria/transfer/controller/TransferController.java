package com.vria.transfer.controller;

import com.vria.transfer.dto.TransferDTO;
import com.vria.transfer.service.TransferService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/transfers")
@RequiredArgsConstructor
@Tag(name = "Transferencias", description = "Comprobantes de pago por transferencia")
@PreAuthorize("hasAnyRole('ADMIN', 'COMMERCIAL_ADVISOR')")
public class TransferController {

    private final TransferService transferService;

    @GetMapping
    public ResponseEntity<List<TransferDTO>> findAll() {
        return ResponseEntity.ok(transferService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.findById(id));
    }

    @PostMapping
    public ResponseEntity<TransferDTO> create(@Valid @RequestBody TransferDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(transferService.create(dto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        transferService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
