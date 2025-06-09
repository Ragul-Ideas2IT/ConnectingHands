package com.connectinghands.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * Data Transfer Object for updating an existing resource.
 * Contains validation annotations to ensure data integrity.
 */
@Data
public class UpdateResourceRequest {
    private String name;
    private String description;
    private String category;
    @Positive(message = "Quantity must be positive")
    private Integer quantity;
    private String unit;
} 