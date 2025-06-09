package com.connectinghands.controller;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.service.OrphanageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orphanages")
@RequiredArgsConstructor
public class OrphanageController {
    private final OrphanageService orphanageService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> createOrphanage(@Valid @RequestBody CreateOrphanageRequest request) {
        return ResponseEntity.ok(orphanageService.createOrphanage(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrphanageDto> getOrphanage(@PathVariable Long id) {
        return ResponseEntity.ok(orphanageService.getOrphanage(id));
    }

    @GetMapping
    public ResponseEntity<List<OrphanageDto>> getAllOrphanages() {
        return ResponseEntity.ok(orphanageService.getAllOrphanages());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrphanageDto> updateOrphanage(@PathVariable Long id, @Valid @RequestBody UpdateOrphanageRequest request) {
        return ResponseEntity.ok(orphanageService.updateOrphanage(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteOrphanage(@PathVariable Long id) {
        orphanageService.deleteOrphanage(id);
        return ResponseEntity.ok().build();
    }
} 