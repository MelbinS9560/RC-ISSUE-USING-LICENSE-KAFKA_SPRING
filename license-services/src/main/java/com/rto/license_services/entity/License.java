package com.rto.license_services.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class License {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String fatherName;
    private String dob;
    private String address;

    @Column(unique = true)
    private String licenseNumber;
    private String issueDate;
    private String bloodGroup;
    private String vehicleClass;
}