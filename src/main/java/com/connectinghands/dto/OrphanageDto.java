package com.connectinghands.dto;

import com.connectinghands.entity.OrphanageStatus;
import lombok.Data;

@Data
public class OrphanageDto {
    private Long id;
    private String name;
    private String description;
    private String address;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String phone;
    private String email;
    private String website;
    private Integer capacity;
    private Integer currentChildren;
    private OrphanageStatus status;
    private Long adminId;
} 