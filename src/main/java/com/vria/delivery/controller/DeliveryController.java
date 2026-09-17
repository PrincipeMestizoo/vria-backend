package com.vria.delivery.controller;

import com.vria.delivery.dto.DeliveryDTO;
import com.vria.delivery.enums.StateDelivery;
import com.vria.delivery.service.DeliveryService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deliveries")
@RequiredArgsConstructor
@Tag(name = "Entregas", description = "Gestion y trazabilidad de entregas a clientes")
@PreAuthorize("hasAnyRole('ADMIN', 'COMMERCIAL_ADVISOR')")
public class DeliveryController {

    private final DeliveryService deliveryService;

    @GetMapping
    public ResponseEntity<List<DeliveryDTO>> findAll() {
        return ResponseEntity.ok(deliveryService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeliveryDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(deliveryService.findById(id));
    }

    @GetMapping("/state/{state}")
    public ResponseEntity<List<DeliveryDTO>> findByState(@PathVariable StateDelivery state) {
        return ResponseEntity.ok(deliveryService.findByState(state));
    }

    @PostMapping
    public ResponseEntity<DeliveryDTO> create(@Valid @RequestBody DeliveryDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(deliveryService.create(dto));
    }

    @PatchMapping("/{id}/state")
    public ResponseEntity<DeliveryDTO> updateState(@PathVariable Long id, @RequestParam StateDelivery state) {
        return ResponseEntity.ok(deliveryService.updateState(id, state));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deliveryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
