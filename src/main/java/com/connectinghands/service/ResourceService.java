package com.connectinghands.service;

import com.connectinghands.dto.CreateResourceRequest;
import com.connectinghands.dto.ResourceDto;
import com.connectinghands.dto.UpdateResourceRequest;

import java.util.List;

/**
 * Service interface for managing resources.
 * Defines operations for creating, reading, updating, and deleting resources.
 *
 * @author Ragul Venkatesan
 */
public interface ResourceService {
    /**
     * Creates a new resource.
     *
     * @param request The request containing resource details
     * @return The created resource DTO
     */
    ResourceDto createResource(CreateResourceRequest request);

    /**
     * Retrieves a resource by its ID.
     *
     * @param id The ID of the resource to retrieve
     * @return The resource DTO
     */
    ResourceDto getResource(Long id);

    /**
     * Retrieves all resources.
     *
     * @return A list of all resource DTOs
     */
    List<ResourceDto> getAllResources();

    /**
     * Updates an existing resource.
     *
     * @param id The ID of the resource to update
     * @param request The request containing updated resource details
     * @return The updated resource DTO
     */
    ResourceDto updateResource(Long id, UpdateResourceRequest request);

    /**
     * Deletes a resource by its ID.
     *
     * @param id The ID of the resource to delete
     */
    void deleteResource(Long id);
} 