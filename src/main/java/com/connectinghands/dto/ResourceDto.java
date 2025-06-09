package com.connectinghands.dto;

import com.connectinghands.entity.ResourceStatus;
import lombok.Data;

/**
 * Data Transfer Object for Resource entities.
 * Used to transfer resource data between layers.
 */
@Data
public class ResourceDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private String unit;
    private Long orphanageId;
    private ResourceStatus status;
} 