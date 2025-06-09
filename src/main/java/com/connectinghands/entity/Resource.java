package com.connectinghands.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * Entity class representing a resource in the system.
 * Resources are items that can be donated or requested by orphanages.
 *
 * @author Ragul Venkatesan
 */
@Data
@Entity
@Table(name = "resources")
@EntityListeners(AuditingEntityListener.class)
public class Resource {
    /**
     * Unique identifier for the resource.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Name of the resource.
     */
    @Column(nullable = false)
    private String name;

    /**
     * Detailed description of the resource.
     */
    @Column(length = 1000)
    private String description;

    /**
     * Category of the resource (e.g., Food, Clothing, Education).
     */
    @Column(nullable = false)
    private String category;

    /**
     * Current quantity of the resource available.
     */
    @Column(nullable = false)
    private Integer quantity;

    /**
     * Unit of measurement for the resource (e.g., pieces, kg, liters).
     */
    @Column(nullable = false)
    private String unit;

    /**
     * The orphanage that owns or manages this resource.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "orphanage_id", nullable = false)
    private Orphanage orphanage;

    /**
     * Current status of the resource.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status;

    /**
     * Timestamp when the resource was created.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Timestamp when the resource was last updated.
     */
    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;
} 