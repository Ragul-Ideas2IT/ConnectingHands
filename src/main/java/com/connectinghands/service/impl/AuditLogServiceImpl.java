package com.connectinghands.service.impl;

import com.connectinghands.dto.AuditLogDto;
import com.connectinghands.entity.AuditLog;
import com.connectinghands.repository.AuditLogRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of the AuditLogService interface.
 * 
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {
    private final AuditLogRepository auditLogRepository;
    private final SecurityService securityService;

    @Override
    @Transactional
    public void logAction(Long userId, String action, String entityType, Long entityId,
                         String oldValue, String newValue, String additionalInfo, String ipAddress) {
        AuditLog log = new AuditLog();
        log.setUserId(userId);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setAdditionalInfo(additionalInfo);
        log.setIpAddress(ipAddress);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }

    @Override
    @Transactional
    public void logAction(String action, String description, Long entityId) {
        Long userId = securityService.getCurrentUserId();
        logAction(userId, action, "Orphanage", entityId, null, null, description, null);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getAuditLogs(Long userId, String action, String entityType, Long entityId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByFilters(userId, action, entityType, entityId, startDate, endDate, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AuditLogDto getAuditLog(Long id) {
        return auditLogRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new RuntimeException("Audit log not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getUserAuditLogs(Long userId, String action, String entityType, Long entityId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByUserIdAndFilters(userId, action, entityType, entityId, startDate, endDate, pageable)
                .map(this::convertToDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditLogDto> getEntityAuditLogs(String entityType, Long entityId, Long userId, String action, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return auditLogRepository.findByEntityTypeAndEntityIdAndFilters(entityType, entityId, userId, action, startDate, endDate, pageable)
                .map(this::convertToDto);
    }

    private AuditLogDto convertToDto(AuditLog auditLog) {
        AuditLogDto dto = new AuditLogDto();
        dto.setId(auditLog.getId());
        dto.setUserId(auditLog.getUserId());
        dto.setAction(auditLog.getAction());
        dto.setEntityType(auditLog.getEntityType());
        dto.setEntityId(auditLog.getEntityId());
        dto.setOldValue(auditLog.getOldValue());
        dto.setNewValue(auditLog.getNewValue());
        dto.setAdditionalInfo(auditLog.getAdditionalInfo());
        dto.setIpAddress(auditLog.getIpAddress());
        dto.setTimestamp(auditLog.getTimestamp());
        return dto;
    }
} 