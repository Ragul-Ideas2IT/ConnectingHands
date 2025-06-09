package com.connectinghands.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Data Transfer Object for updating an existing resource.
 * Contains validation annotations to ensure data integrity.
 *
 * @author Ragul Venkatesan
 */
@Data
public class UpdateResourceRequest {
    /**
     * Updated name of the resource.
     */
    private String name;

    /**
     * Updated description of the resource.
     */
    private String description;

    /**
     * Updated category of the resource.
     */
    private String category;

    /**
     * Updated quantity of the resource.
     * Must be a positive number if provided.
     */
    @Positive(message = "Quantity must be positive")
    private Integer quantity;

    /**
     * Updated unit of measurement for the resource.
     */
    private String unit;
} 