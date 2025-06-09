package com.connectinghands.dto;

import com.connectinghands.entity.ResourceStatus;
import lombok.Data;

/**
 * Data Transfer Object for Resource entities.
 * Used to transfer resource data between layers of the application.
 *
 * @author Ragul Venkatesan
 */
@Data
public class ResourceDto {
    /**
     * Unique identifier of the resource.
     */
    private Long id;

    /**
     * Name of the resource.
     */
    private String name;

    /**
     * Detailed description of the resource.
     */
    private String description;

    /**
     * Category of the resource.
     */
    private String category;

    /**
     * Current quantity of the resource.
     */
    private Integer quantity;

    /**
     * Unit of measurement for the resource.
     */
    private String unit;

    /**
     * ID of the orphanage that owns this resource.
     */
    private Long orphanageId;

    /**
     * Current status of the resource.
     */
    private ResourceStatus status;
} 