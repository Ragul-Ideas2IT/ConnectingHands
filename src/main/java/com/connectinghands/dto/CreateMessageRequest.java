package com.connectinghands.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Request DTO for creating a new message.
 * 
 * @author Ragul Venkatesan
 */
@Data
public class CreateMessageRequest {
    @NotNull(message = "Receiver ID is required")
    private Long receiverId;

    @NotBlank(message = "Message content is required")
    @Size(min = 1, max = 1000, message = "Message content must be between 1 and 1000 characters")
    private String content;
} 