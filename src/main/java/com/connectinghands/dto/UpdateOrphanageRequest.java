package com.connectinghands.dto;

import com.connectinghands.entity.OrphanageStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Data Transfer Object for updating an existing orphanage.
 * 
 * @author Ragul Venkatesan
 */
@Data
public class UpdateOrphanageRequest {
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Size(max = 200, message = "Address cannot exceed 200 characters")
    private String address;

    @Size(max = 100, message = "City cannot exceed 100 characters")
    private String city;

    @Size(max = 100, message = "State cannot exceed 100 characters")
    private String state;

    @Size(max = 100, message = "Country cannot exceed 100 characters")
    private String country;

    @Pattern(regexp = "^[0-9]{5,10}$", message = "Postal code must be between 5 and 10 digits")
    private String postalCode;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number must be between 10 and 15 digits")
    private String phone;

    @Email(message = "Invalid email format")
    @Size(max = 100, message = "Email cannot exceed 100 characters")
    private String email;

    @Pattern(regexp = "^(https?://)?([\\da-z.-]+)\\.([a-z.]{2,6})([/\\w .-]*)*/?$", 
             message = "Invalid website URL format")
    private String website;

    @Positive(message = "Capacity must be a positive number")
    private Integer capacity;

    private OrphanageStatus status;

    private String verificationDocuments;

    private String verificationNotes;
} 