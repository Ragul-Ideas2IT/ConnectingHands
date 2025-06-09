package com.connectinghands.dto;

import com.connectinghands.entity.ResourceRequestStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning resource request data.
 * Used to transfer resource request information between layers.
 *
 * @author Ragul Venkatesan
 */
@Data
public class ResourceRequestDto {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Integer quantity;
    private String unit;
    private Long orphanageId;
    private ResourceRequestStatus status;
    private Long fulfilledBy;
    private LocalDateTime fulfilledAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 