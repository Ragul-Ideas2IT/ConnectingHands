package com.connectinghands.dto;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * DTO for transferring message data.
 * 
 * @author Ragul Venkatesan
 */
@Data
public class MessageDto {
    private Long id;
    private Long senderId;
    private String senderName;
    private Long receiverId;
    private String receiverName;
    private String content;
    private boolean read;
    private LocalDateTime readAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 