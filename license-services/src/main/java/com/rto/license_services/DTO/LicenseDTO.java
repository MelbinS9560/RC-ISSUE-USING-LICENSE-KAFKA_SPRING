package com.rto.license_services.DTO;

import lombok.Data;

@Data
public class LicenseDTO {
    private Long id;
    private String name;
    private String fatherName;
    private String dob;
    private String address; // You can omit this in the GET mapping later if needed
    private String licenseNumber;
    private String issueDate;
    private String bloodGroup;
    private String vehicleClass;
    private String status; // Calculated field

}