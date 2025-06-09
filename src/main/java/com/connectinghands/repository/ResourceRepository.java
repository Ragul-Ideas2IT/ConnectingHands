package com.connectinghands.repository;

import com.connectinghands.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for managing Resource entities.
 * Provides methods for CRUD operations on resources.
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
} 