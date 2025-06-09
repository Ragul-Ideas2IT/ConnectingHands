package com.connectinghands.service;

import com.connectinghands.entity.AuditLog;

public interface AuditLogService {
    AuditLog logAction(Long userId, String action, String entityType, Long entityId, String oldValue, String newValue, String ipAddress, String userAgent);
} 