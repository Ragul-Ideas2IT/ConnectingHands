package com.connectinghands.service.impl;

import com.connectinghands.entity.AuditLog;
import com.connectinghands.entity.User;
import com.connectinghands.repository.AuditLogRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.AuditLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public AuditLog logAction(Long userId, String action, String entityType, Long entityId, String oldValue, String newValue, String ipAddress, String userAgent) {
        AuditLog log = new AuditLog();
        if (userId != null) {
            User user = userRepository.findById(userId).orElse(null);
            log.setUser(user);
        }
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setOldValue(oldValue);
        log.setNewValue(newValue);
        log.setIpAddress(ipAddress);
        log.setUserAgent(userAgent);
        return auditLogRepository.save(log);
    }
} 