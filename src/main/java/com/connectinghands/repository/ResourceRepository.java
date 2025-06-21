package com.connectinghands.repository;

import com.connectinghands.entity.Resource;
import com.connectinghands.entity.ResourceCategory;
import com.connectinghands.entity.ResourceStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for managing Resource entities.
 * Provides CRUD operations and custom queries for resources.
 *
 * @author Ragul Venkatesan
 */
@Repository
public interface ResourceRepository extends JpaRepository<Resource, Long> {
    /**
     * Finds all resources owned by a specific orphanage.
     *
     * @param orphanageId The ID of the orphanage
     * @return List of resources owned by the orphanage
     */
    List<Resource> findByOrphanageId(Long orphanageId);

    List<Resource> findByCategory(ResourceCategory category);
    List<Resource> findByStatus(ResourceStatus status);
} 