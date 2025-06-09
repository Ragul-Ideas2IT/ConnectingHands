package com.connectinghands.service.impl;

import com.connectinghands.entity.AuditLog;
import com.connectinghands.repository.AuditLogRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.SecurityService;
import lombok.RequiredArgsConstructor;
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
        AuditLog log = new AuditLog();
        log.setUserId(securityService.getCurrentUserId());
        log.setAction(action);
        log.setEntityType("Orphanage");
        log.setEntityId(entityId);
        log.setAdditionalInfo(description);
        log.setTimestamp(LocalDateTime.now());
        auditLogRepository.save(log);
    }
} 