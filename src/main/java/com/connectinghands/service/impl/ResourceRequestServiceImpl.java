package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceRequestDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.ResourceRequest;
import com.connectinghands.entity.ResourceRequestStatus;
import com.connectinghands.exception.ResourceNotFoundException;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRequestRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.ResourceRequestService;
import com.connectinghands.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of ResourceRequestService.
 * Handles CRUD operations for resource requests with audit logging.
 *
 * @author Ragul Venkatesan
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ResourceRequestServiceImpl implements ResourceRequestService {
    private final ResourceRequestRepository resourceRequestRepository;
    private final OrphanageRepository orphanageRepository;
    private final SecurityService securityService;
    private final AuditLogService auditLogService;

    @Override
    @Transactional
    public ResourceRequestDto createResourceRequest(CreateResourceRequest request) {
        log.info("Creating new resource request for orphanage: {}", request.getOrphanageId());
        
        Orphanage orphanage = orphanageRepository.findById(request.getOrphanageId())
                .orElseThrow(() -> new ResourceNotFoundException("Orphanage not found"));

        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setName(request.getName());
        resourceRequest.setDescription(request.getDescription());
        resourceRequest.setCategory(request.getCategory());
        resourceRequest.setQuantity(request.getQuantity());
        resourceRequest.setUnit(request.getUnit());
        resourceRequest.setOrphanage(orphanage);
        resourceRequest.setStatus(ResourceRequestStatus.PENDING);

        ResourceRequest savedRequest = resourceRequestRepository.save(resourceRequest);
        
        auditLogService.logAction(
            "CREATE_RESOURCE_REQUEST",
            "Created resource request: " + savedRequest.getId(),
            savedRequest.getId()
        );

        return mapToDto(savedRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceRequestDto getResourceRequest(Long id) {
        log.info("Retrieving resource request: {}", id);
        
        ResourceRequest resourceRequest = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found"));

        return mapToDto(resourceRequest);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestDto> getAllResourceRequests() {
        log.info("Retrieving all resource requests");
        
        return resourceRequestRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestDto> getResourceRequestsByOrphanage(Long orphanageId) {
        log.info("Retrieving resource requests for orphanage: {}", orphanageId);
        
        return resourceRequestRepository.findByOrphanageId(orphanageId).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceRequestDto> getResourceRequestsByStatus(ResourceRequestStatus status) {
        log.info("Retrieving resource requests with status: {}", status);
        
        return resourceRequestRepository.findByStatus(status).stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResourceRequestDto updateResourceRequest(Long id, UpdateResourceRequest request) {
        log.info("Updating resource request: {}", id);
        
        ResourceRequest resourceRequest = resourceRequestRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Resource request not found"));

        if (request.getName() != null) {
            resourceRequest.setName(request.getName());
        }
        if (request.getDescription() != null) {
            resourceRequest.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            resourceRequest.setCategory(request.getCategory());
        }
        if (request.getQuantity() != null) {
            resourceRequest.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            resourceRequest.setUnit(request.getUnit());
        }
        if (request.getStatus() != null) {
            resourceRequest.setStatus(request.getStatus());
            if (request.getStatus() == ResourceRequestStatus.FULFILLED) {
                resourceRequest.setFulfilledBy(securityService.getCurrentUserId());
                resourceRequest.setFulfilledAt(LocalDateTime.now());
            }
        }

        ResourceRequest updatedRequest = resourceRequestRepository.save(resourceRequest);
        
        auditLogService.logAction(
            "UPDATE_RESOURCE_REQUEST",
            "Updated resource request: " + id,
            id
        );

        return mapToDto(updatedRequest);
    }

    @Override
    @Transactional
    public void deleteResourceRequest(Long id) {
        log.info("Deleting resource request: {}", id);
        
        if (!resourceRequestRepository.existsById(id)) {
            throw new ResourceNotFoundException("Resource request not found");
        }

        resourceRequestRepository.deleteById(id);
        
        auditLogService.logAction(
            "DELETE_RESOURCE_REQUEST",
            "Deleted resource request: " + id,
            id
        );
    }

    private ResourceRequestDto mapToDto(ResourceRequest request) {
        ResourceRequestDto dto = new ResourceRequestDto();
        dto.setId(request.getId());
        dto.setName(request.getName());
        dto.setDescription(request.getDescription());
        dto.setCategory(request.getCategory());
        dto.setQuantity(request.getQuantity());
        dto.setUnit(request.getUnit());
        dto.setOrphanageId(request.getOrphanage().getId());
        dto.setStatus(request.getStatus());
        dto.setFulfilledBy(request.getFulfilledBy());
        dto.setFulfilledAt(request.getFulfilledAt());
        dto.setCreatedAt(request.getCreatedAt());
        dto.setUpdatedAt(request.getUpdatedAt());
        return dto;
    }
} 