package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.OrphanageStatus;
import com.connectinghands.entity.User;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.OrphanageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrphanageServiceImpl implements OrphanageService {

    private final OrphanageRepository orphanageRepository;
    private final UserRepository userRepository;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public OrphanageDto createOrphanage(CreateOrphanageRequest request) {
        User admin = userRepository.findById(request.getAdminId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + request.getAdminId()));

        Orphanage orphanage = new Orphanage();
        orphanage.setName(request.getName());
        orphanage.setDescription(request.getDescription());
        orphanage.setAddress(request.getAddress());
        orphanage.setCity(request.getCity());
        orphanage.setState(request.getState());
        orphanage.setCountry(request.getCountry());
        orphanage.setPostalCode(request.getPostalCode());
        orphanage.setPhone(request.getPhone());
        orphanage.setEmail(request.getEmail());
        orphanage.setWebsite(request.getWebsite());
        orphanage.setCapacity(request.getCapacity());
        orphanage.setCurrentChildren(0);
        orphanage.setStatus(OrphanageStatus.PENDING);
        orphanage.setAdmin(admin);

        orphanage = orphanageRepository.save(orphanage);
        auditLogService.logAction(admin.getId(), "CREATE_ORPHANAGE", "Orphanage", orphanage.getId(), null, null, null, null);
        return mapToDto(orphanage);
    }

    @Override
    public OrphanageDto getOrphanage(Long id) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found with id: " + id));
        return mapToDto(orphanage);
    }

    @Override
    public List<OrphanageDto> getAllOrphanages() {
        return orphanageRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public OrphanageDto updateOrphanage(Long id, UpdateOrphanageRequest request) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found with id: " + id));

        String oldValue = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"address\":\"%s\",\"city\":\"%s\",\"state\":\"%s\",\"country\":\"%s\",\"postalCode\":\"%s\",\"phone\":\"%s\",\"email\":\"%s\",\"website\":\"%s\",\"capacity\":%d,\"currentChildren\":%d}",
                orphanage.getName(), orphanage.getDescription(), orphanage.getAddress(), orphanage.getCity(),
                orphanage.getState(), orphanage.getCountry(), orphanage.getPostalCode(), orphanage.getPhone(),
                orphanage.getEmail(), orphanage.getWebsite(), orphanage.getCapacity(), orphanage.getCurrentChildren());

        if (request.getName() != null) orphanage.setName(request.getName());
        if (request.getDescription() != null) orphanage.setDescription(request.getDescription());
        if (request.getAddress() != null) orphanage.setAddress(request.getAddress());
        if (request.getCity() != null) orphanage.setCity(request.getCity());
        if (request.getState() != null) orphanage.setState(request.getState());
        if (request.getCountry() != null) orphanage.setCountry(request.getCountry());
        if (request.getPostalCode() != null) orphanage.setPostalCode(request.getPostalCode());
        if (request.getPhone() != null) orphanage.setPhone(request.getPhone());
        if (request.getEmail() != null) orphanage.setEmail(request.getEmail());
        if (request.getWebsite() != null) orphanage.setWebsite(request.getWebsite());
        if (request.getCapacity() != null) orphanage.setCapacity(request.getCapacity());
        if (request.getCurrentChildren() != null) orphanage.setCurrentChildren(request.getCurrentChildren());

        orphanage = orphanageRepository.save(orphanage);
        String newValue = String.format("{\"name\":\"%s\",\"description\":\"%s\",\"address\":\"%s\",\"city\":\"%s\",\"state\":\"%s\",\"country\":\"%s\",\"postalCode\":\"%s\",\"phone\":\"%s\",\"email\":\"%s\",\"website\":\"%s\",\"capacity\":%d,\"currentChildren\":%d}",
                orphanage.getName(), orphanage.getDescription(), orphanage.getAddress(), orphanage.getCity(),
                orphanage.getState(), orphanage.getCountry(), orphanage.getPostalCode(), orphanage.getPhone(),
                orphanage.getEmail(), orphanage.getWebsite(), orphanage.getCapacity(), orphanage.getCurrentChildren());

        auditLogService.logAction(orphanage.getAdmin().getId(), "UPDATE_ORPHANAGE", "Orphanage", orphanage.getId(), oldValue, newValue, null, null);
        return mapToDto(orphanage);
    }

    @Override
    @Transactional
    public void deleteOrphanage(Long id) {
        Orphanage orphanage = orphanageRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found with id: " + id));
        orphanageRepository.delete(orphanage);
        auditLogService.logAction(orphanage.getAdmin().getId(), "DELETE_ORPHANAGE", "Orphanage", orphanage.getId(), null, null, null, null);
    }

    private OrphanageDto mapToDto(Orphanage orphanage) {
        OrphanageDto dto = new OrphanageDto();
        dto.setId(orphanage.getId());
        dto.setName(orphanage.getName());
        dto.setDescription(orphanage.getDescription());
        dto.setAddress(orphanage.getAddress());
        dto.setCity(orphanage.getCity());
        dto.setState(orphanage.getState());
        dto.setCountry(orphanage.getCountry());
        dto.setPostalCode(orphanage.getPostalCode());
        dto.setPhone(orphanage.getPhone());
        dto.setEmail(orphanage.getEmail());
        dto.setWebsite(orphanage.getWebsite());
        dto.setCapacity(orphanage.getCapacity());
        dto.setCurrentChildren(orphanage.getCurrentChildren());
        dto.setStatus(orphanage.getStatus());
        dto.setAdminId(orphanage.getAdmin().getId());
        return dto;
    }
} 