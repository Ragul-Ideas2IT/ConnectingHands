package com.connectinghands.service;

import com.connectinghands.dto.CreateOrphanageRequest;
import com.connectinghands.dto.OrphanageDto;
import com.connectinghands.dto.UpdateOrphanageRequest;

import java.util.List;

public interface OrphanageService {
    OrphanageDto createOrphanage(CreateOrphanageRequest request);
    OrphanageDto getOrphanage(Long id);
    List<OrphanageDto> getAllOrphanages();
    OrphanageDto updateOrphanage(Long id, UpdateOrphanageRequest request);
    void deleteOrphanage(Long id);
} 