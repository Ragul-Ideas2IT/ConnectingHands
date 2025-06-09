package com.connectinghands.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class UpdateOrphanageRequest {
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    @Email(message = "Invalid email format")
    private String email;
    private String website;
    @Positive(message = "Capacity must be positive")
    private Integer capacity;
    private Integer currentChildren;
} 