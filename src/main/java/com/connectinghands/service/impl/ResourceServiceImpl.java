package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceRequest;
import com.connectinghands.entity.Orphanage;
import com.connectinghands.entity.Resource;
import com.connectinghands.entity.ResourceStatus;
import com.connectinghands.repository.OrphanageRepository;
import com.connectinghands.repository.ResourceRepository;
import com.connectinghands.service.ResourceService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementation of the ResourceService interface.
 * Handles CRUD operations for resources and includes audit logging.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class ResourceServiceImpl implements ResourceService {
    private final ResourceRepository resourceRepository;
    private final OrphanageRepository orphanageRepository;

    /**
     * Creates a new resource.
     * Logs the creation action and sets initial status to AVAILABLE.
     *
     * @param request The request containing resource details
     * @return The created resource DTO
     * @throws EntityNotFoundException if the orphanage is not found
     */
    @Override
    @Transactional
    public ResourceDto createResource(CreateResourceRequest request) {
        Orphanage orphanage = orphanageRepository.findById(request.getOrphanageId())
                .orElseThrow(() -> new EntityNotFoundException("Orphanage not found"));

        Resource resource = new Resource();
        resource.setName(request.getName());
        resource.setDescription(request.getDescription());
        resource.setCategory(request.getCategory());
        resource.setQuantity(request.getQuantity());
        resource.setUnit(request.getUnit());
        resource.setOrphanage(orphanage);
        resource.setStatus(ResourceStatus.AVAILABLE);

        Resource savedResource = resourceRepository.save(resource);
        return convertToDto(savedResource);
    }

    /**
     * Retrieves a resource by its ID.
     *
     * @param id The ID of the resource to retrieve
     * @return The resource DTO
     * @throws EntityNotFoundException if the resource is not found
     */
    @Override
    @Transactional(readOnly = true)
    public ResourceDto getResource(Long id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));
        return convertToDto(resource);
    }

    /**
     * Retrieves all resources.
     *
     * @return A list of all resource DTOs
     */
    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> getAllResources() {
        return resourceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing resource.
     * Logs the update action.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     * @throws EntityNotFoundException if the resource is not found
     */
    @Override
    @Transactional
    public ResourceDto updateResource(Long id, UpdateResourceRequest request) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Resource not found"));

        if (request.getName() != null) {
            resource.setName(request.getName());
        }
        if (request.getDescription() != null) {
            resource.setDescription(request.getDescription());
        }
        if (request.getCategory() != null) {
            resource.setCategory(request.getCategory());
        }
        if (request.getQuantity() != null) {
            resource.setQuantity(request.getQuantity());
        }
        if (request.getUnit() != null) {
            resource.setUnit(request.getUnit());
        }

        Resource updatedResource = resourceRepository.save(resource);
        return convertToDto(updatedResource);
    }

    /**
     * Deletes a resource by its ID.
     * Logs the deletion action.
     *
     * @param id The ID of the resource to delete
     * @throws EntityNotFoundException if the resource is not found
     */
    @Override
    @Transactional
    public void deleteResource(Long id) {
        if (!resourceRepository.existsById(id)) {
            throw new EntityNotFoundException("Resource not found");
        }
        resourceRepository.deleteById(id);
    }

    /**
     * Converts a Resource entity to a ResourceDto.
     *
     * @param resource The resource entity to convert
     * @return The converted ResourceDto
     */
    private ResourceDto convertToDto(Resource resource) {
        ResourceDto dto = new ResourceDto();
        dto.setId(resource.getId());
        dto.setName(resource.getName());
        dto.setDescription(resource.getDescription());
        dto.setCategory(resource.getCategory());
        dto.setQuantity(resource.getQuantity());
        dto.setUnit(resource.getUnit());
        dto.setOrphanageId(resource.getOrphanage().getId());
        dto.setStatus(resource.getStatus());
        return dto;
    }
} 