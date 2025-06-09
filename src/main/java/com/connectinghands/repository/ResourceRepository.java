package com.connectinghands.repository;

import com.connectinghands.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Resource entities.
 * Provides CRUD operations and custom queries for resources.
 *
 * @author Ragul Venkatesan
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    // Custom queries can be added here as needed
} 