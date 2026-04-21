package com.rto.rc_services.DTO;


import lombok.Data;

@Data
public class RCDTO {
    private Long id;
    private String ownerName;
    private String address;
    private String registrationNumber;
    private String licenseNumber;
    private String vehicleType;
    private String rtoCode;
    private String registrationDate;
    private String engineNumber;
    private String chassisNumber;
    private String model;
    private String fuelType;
    private String color;
    private String expiryDate;
    private String status; // Calculated field for the UI/Client
}