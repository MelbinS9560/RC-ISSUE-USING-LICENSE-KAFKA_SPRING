package com.rto.rc_services.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class RC {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String ownerName;
	private String address;
	@Column(name = "license_number", nullable = false)
	private String licenseNumber;
	private String vehicleType;

	@Column(unique = true)
	private String registrationNumber;
	private String rtoCode;
	private String registrationDate;
	private String engineNumber;
	private String chassisNumber;
	private String model;
	private String fuelType;
	private String color;
	private String expiryDate;
}