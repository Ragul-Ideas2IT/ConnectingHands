package com.connectinghands.service.impl;

import com.connectinghands.dto.CreateMessageRequest;
import com.connectinghands.dto.MessageDto;
import com.connectinghands.entity.Message;
import com.connectinghands.entity.User;
import com.connectinghands.repository.MessageRepository;
import com.connectinghands.repository.UserRepository;
import com.connectinghands.service.AuditLogService;
import com.connectinghands.service.MessageService;
import com.connectinghands.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * Implementation of MessageService for managing messages between users.
 * Handles sending, retrieving, and marking messages as read, with audit logging.
 *
 * @author Ragul Venkatesan
 */
@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final SecurityService securityService;
    private final AuditLogService auditLogService;

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public MessageDto sendMessage(CreateMessageRequest request) {
        Long senderId = securityService.getCurrentUserId();
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new IllegalArgumentException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new IllegalArgumentException("Receiver not found"));
        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setRead(false);
        message.setCreatedAt(LocalDateTime.now());
        Message saved = messageRepository.save(message);
        auditLogService.logAction(senderId, "MESSAGE_SENT", "Message", saved.getId(), null, saved.getContent(), null, null);
        return toDto(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getConversation(Long otherUserId, Pageable pageable) {
        Long currentUserId = securityService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
        User otherUser = userRepository.findById(otherUserId)
                .orElseThrow(() -> new IllegalArgumentException("Other user not found"));
        return messageRepository.findBySenderAndReceiverOrReceiverAndSenderOrderByCreatedAtDesc(
                currentUser, otherUser, otherUser, currentUser, pageable)
                .map(this::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getInbox(Pageable pageable) {
        Long currentUserId = securityService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
        return messageRepository.findByReceiverOrderByCreatedAtDesc(currentUser, pageable)
                .map(this::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<MessageDto> getSent(Pageable pageable) {
        Long currentUserId = securityService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
        return messageRepository.findBySenderOrderByCreatedAtDesc(currentUser, pageable)
                .map(this::toDto);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void markAsRead(Long messageId) {
        Long currentUserId = securityService.getCurrentUserId();
        Message message = messageRepository.findById(messageId)
                .orElseThrow(() -> new IllegalArgumentException("Message not found"));
        if (!message.getReceiver().getId().equals(currentUserId)) {
            throw new SecurityException("You are not authorized to mark this message as read");
        }
        if (!message.isRead()) {
            message.setRead(true);
            message.setReadAt(LocalDateTime.now());
            messageRepository.save(message);
            auditLogService.logAction(currentUserId, "MESSAGE_READ", "Message", messageId, null, null, null, null);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long countUnread() {
        Long currentUserId = securityService.getCurrentUserId();
        User currentUser = userRepository.findById(currentUserId)
                .orElseThrow(() -> new IllegalArgumentException("Current user not found"));
        return messageRepository.countByReceiverAndReadFalse(currentUser);
    }

    /**
     * Convert Message entity to MessageDto.
     */
    private MessageDto toDto(Message message) {
        MessageDto dto = new MessageDto();
        dto.setId(message.getId());
        dto.setSenderId(message.getSender().getId());
        dto.setSenderName(message.getSender().getFirstName() + " " + message.getSender().getLastName());
        dto.setReceiverId(message.getReceiver().getId());
        dto.setReceiverName(message.getReceiver().getFirstName() + " " + message.getReceiver().getLastName());
        dto.setContent(message.getContent());
        dto.setRead(message.isRead());
        dto.setReadAt(message.getReadAt());
        dto.setCreatedAt(message.getCreatedAt());
        dto.setUpdatedAt(message.getUpdatedAt());
        return dto;
    }
} 