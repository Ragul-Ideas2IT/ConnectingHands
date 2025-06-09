package com.connectinghands.service;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service interface for managing messages between users (orphanages).
 * Provides methods for sending, retrieving, and marking messages as read.
 *
 * @author Ragul Venkatesan
 */
public interface MessageService {
    /**
     * Send a new message from the current user to a receiver.
     *
     * @param request The message creation request
     * @return The created message DTO
     */
    MessageDto sendMessage(CreateMessageRequest request);

    /**
     * Get a paginated list of messages between the current user and another user.
     *
     * @param otherUserId The other user's ID
     * @param pageable Pagination information
     * @return Page of message DTOs
     */
    Page<MessageDto> getConversation(Long otherUserId, Pageable pageable);

    /**
     * Get a paginated list of messages received by the current user.
     *
     * @param pageable Pagination information
     * @return Page of message DTOs
     */
    Page<MessageDto> getInbox(Pageable pageable);

    /**
     * Get a paginated list of messages sent by the current user.
     *
     * @param pageable Pagination information
     * @return Page of message DTOs
     */
    Page<MessageDto> getSent(Pageable pageable);

    /**
     * Mark a message as read by the current user.
     *
     * @param messageId The message ID
     */
    void markAsRead(Long messageId);

    /**
     * Count the number of unread messages for the current user.
     *
     * @return Number of unread messages
     */
    long countUnread();
} 