package com.rto.rc_services.service;

import com.rto.rc_services.DTO.RCDTO;
import com.rto.rc_services.entity.RC;
import com.rto.rc_services.repository.RCRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RCService {

    @Autowired
    private RCRepository repo;

    // This is the new logic that the Kafka Listener will call
    public void validateRCByVehicleType(String licenseNumber, String vehicleType) {
        System.out.println(vehicleType + " SERVICE: Searching for License: " + licenseNumber);

        List<RC> rcList = repo.findByLicenseNumberAndVehicleType(licenseNumber, vehicleType);

        if (!rcList.isEmpty()) {
            System.out.println(vehicleType + " MATCH FOUND! Total Vehicles: " + rcList.size());

            for (RC rc : rcList) {
                System.out.println("\n-------------------------------------------");
                System.out.println("   VEHICLE RECORD FOUND (" + vehicleType + ")");
                System.out.println("-------------------------------------------");
                System.out.println("Owner          : " + rc.getOwnerName());
                System.out.println("Reg No         : " + rc.getRegistrationNumber());
                System.out.println("Model          : " + rc.getModel());
                System.out.println("Address        : " + rc.getAddress());
                System.out.println("Chassis No     : " + rc.getChassisNumber());
                System.out.println("Engine No      : " + rc.getEngineNumber());
                System.out.println("Fuel Type      : " + rc.getFuelType());
                System.out.println("Color          : " + rc.getColor());
                System.out.println("Expiry Date    : " + rc.getExpiryDate());
                System.out.println("RTO Code       : " + rc.getRtoCode());
                System.out.println("-------------------------------------------\n");
            }
        } else {
            System.out.println(vehicleType + " NOT FOUND for License: " + licenseNumber);
        }
    }

    // Helper method to convert Entity to DTO
    private RCDTO convertToDto(RC rc) {
        RCDTO dto = new RCDTO();
        dto.setId(rc.getId());
        dto.setOwnerName(rc.getOwnerName());
        dto.setAddress(rc.getAddress());
        dto.setRegistrationNumber(rc.getRegistrationNumber());
        dto.setRtoCode(rc.getRtoCode());
        dto.setRegistrationDate(rc.getRegistrationDate());
        dto.setEngineNumber(rc.getEngineNumber());
        dto.setChassisNumber(rc.getChassisNumber());
        dto.setModel(rc.getModel());
        dto.setFuelType(rc.getFuelType());
        dto.setColor(rc.getColor());
        dto.setExpiryDate(rc.getExpiryDate());
        dto.setLicenseNumber(rc.getLicenseNumber());
        dto.setVehicleType(rc.getVehicleType());

        // Logic for calculated field
        LocalDate expiry = LocalDate.parse(rc.getExpiryDate());
        dto.setStatus(expiry.isBefore(LocalDate.now()) ? "EXPIRED" : "ACTIVE");

        return dto;
    }

    // Helper method to convert DTO to Entity
    private void mapToEntity(RCDTO dto, RC rc) {
        rc.setOwnerName(dto.getOwnerName());
        rc.setAddress(dto.getAddress());
        rc.setRegistrationNumber(dto.getRegistrationNumber());
        rc.setRtoCode(dto.getRtoCode());
        rc.setRegistrationDate(dto.getRegistrationDate());
        rc.setEngineNumber(dto.getEngineNumber());
        rc.setChassisNumber(dto.getChassisNumber());
        rc.setModel(dto.getModel());
        rc.setFuelType(dto.getFuelType());
        rc.setColor(dto.getColor());
        rc.setExpiryDate(dto.getExpiryDate());
        rc.setLicenseNumber(dto.getLicenseNumber());
        rc.setVehicleType(dto.getVehicleType());
    }

    public String save(RCDTO rcDto) {
        if (repo.findByRegistrationNumber(rcDto.getRegistrationNumber()).isPresent()) {
            return "RC already exists!";
        }
        RC rc = new RC();
        mapToEntity(rcDto, rc);
        repo.save(rc);
        return "RC saved successfully";
    }

    public List<RCDTO> getAll() {
        return repo.findAll().stream().map(this::convertToDto).collect(Collectors.toList());
    }

    public RCDTO getByRegistrationNumber(String regNo) {
        RC rc = repo.findByRegistrationNumber(regNo)
                .orElseThrow(() -> new RuntimeException("RC not found"));
        return convertToDto(rc);
    }

    // Get by ID (Throws Kafka ResourceNotFoundException)
    public RCDTO getById(Long id) {
        RC rc = repo.findById(id)
                .orElseThrow(() -> new org.apache.kafka.common.errors.ResourceNotFoundException("RC not found with ID: " + id));
        return convertToDto(rc);
    }

    public String updateByRegistrationNumber(String regNo, RCDTO updatedDto) {
        RC existing = repo.findByRegistrationNumber(regNo)
                .orElseThrow(() -> new RuntimeException("RC not found"));

        mapToEntity(updatedDto, existing);
        repo.save(existing);
        return "RC updated successfully";
    }

    public String delete(Long id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return "RC deleted successfully";
        }
        return "RC not found";
    }

    public String deleteByRegistrationNumber(String regNo) {
        RC rc = repo.findByRegistrationNumber(regNo)
                .orElseThrow(() -> new RuntimeException("RC not found"));
        repo.delete(rc);
        return "RC deleted successfully";
    }
}