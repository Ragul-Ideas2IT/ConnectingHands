package com.connectinghands.controller;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import com.connectinghands.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for managing messages between users.
 * Provides endpoints for sending, retrieving, and marking messages as read.
 *
 * @author Ragul Venkatesan
 */
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {
    private final MessageService messageService;

    /**
     * Send a new message to another user.
     * Requires authentication.
     *
     * @param request The message creation request
     * @return The created message
     */
    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<MessageDto> sendMessage(@Valid @RequestBody CreateMessageRequest request) {
        return ResponseEntity.ok(messageService.sendMessage(request));
    }

    /**
     * Get a paginated conversation between the current user and another user.
     * Requires authentication.
     *
     * @param otherUserId The other user's ID
     * @param pageable Pagination information
     * @return Page of messages
     */
    @GetMapping("/conversation/{otherUserId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MessageDto>> getConversation(
            @PathVariable Long otherUserId,
            Pageable pageable) {
        return ResponseEntity.ok(messageService.getConversation(otherUserId, pageable));
    }

    /**
     * Get a paginated list of messages received by the current user.
     * Requires authentication.
     *
     * @param pageable Pagination information
     * @return Page of messages
     */
    @GetMapping("/inbox")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MessageDto>> getInbox(Pageable pageable) {
        return ResponseEntity.ok(messageService.getInbox(pageable));
    }

    /**
     * Get a paginated list of messages sent by the current user.
     * Requires authentication.
     *
     * @param pageable Pagination information
     * @return Page of messages
     */
    @GetMapping("/sent")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<MessageDto>> getSent(Pageable pageable) {
        return ResponseEntity.ok(messageService.getSent(pageable));
    }

    /**
     * Mark a message as read.
     * Requires authentication and the message must be received by the current user.
     *
     * @param messageId The message ID
     * @return No content response
     */
    @PutMapping("/{messageId}/read")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> markAsRead(@PathVariable Long messageId) {
        messageService.markAsRead(messageId);
        return ResponseEntity.noContent().build();
    }

    /**
     * Get the count of unread messages for the current user.
     * Requires authentication.
     *
     * @return The number of unread messages
     */
    @GetMapping("/unread/count")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Long> getUnreadCount() {
        return ResponseEntity.ok(messageService.countUnread());
    }
} 