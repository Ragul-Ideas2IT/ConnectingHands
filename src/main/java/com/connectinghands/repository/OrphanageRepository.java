package com.connectinghands.repository;

import com.connectinghands.entity.Orphanage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrphanageRepository extends JpaRepository<Orphanage, Long> {
} 