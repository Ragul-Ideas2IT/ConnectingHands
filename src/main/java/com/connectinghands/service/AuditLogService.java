package com.connectinghands.service;

/**
 * Service interface for managing audit logs.
 * 
 * @author Ragul Venkatesan
 */
public interface AuditLogService {
    /**
     * Log an action with detailed information.
     *
     * @param userId the ID of the user performing the action
     * @param action the action being performed
     * @param entityType the type of entity being acted upon
     * @param entityId the ID of the entity being acted upon
     * @param oldValue the previous value (if applicable)
     * @param newValue the new value (if applicable)
     * @param additionalInfo additional information about the action
     * @param ipAddress the IP address of the user
     */
    void logAction(Long userId, String action, String entityType, Long entityId,
                  String oldValue, String newValue, String additionalInfo, String ipAddress);

    /**
     * Log a simple action.
     *
     * @param action the action being performed
     * @param description a description of the action
     * @param entityId the ID of the entity being acted upon
     */
    void logAction(String action, String description, Long entityId);
} 