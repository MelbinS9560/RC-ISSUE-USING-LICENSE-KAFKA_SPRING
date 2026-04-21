package com.rto.license_services.service;

import com.rto.license_services.DTO.LicenseDTO;
import com.rto.license_services.entity.License;
import com.rto.license_services.repository.LicenseRepository;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LicenseService {


//    public String sendMessage(LicenseDTO dto)
//    {
//        kafkaTemplate.send("test-topic","LicenseDTO",dto);
//        return("Message send to Kafka server by producer service");
//    }
//
//public String sendMessage(LicenseDTO dto) {
//    try {
//        // This will trigger the actual connection and serialization
//        kafkaTemplate.send("test-topic", dto.getLicenseNumber(), dto);
//        return "Message sent to Kafka server by Producer successfully";
//    } catch (Exception e) {
//        // This will print the RED text in your console that tells us exactly what failed
//        e.printStackTrace();
//        return "Failed to send message: " + e.getMessage();
//    }
//}
@Autowired
private LicenseRepository repo;

    // Updated to String, String because we are only sending the license number
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public String broadcastLicenseForRC(String licenseNumber) {
        try {
            // 1. Send ONLY the license number string to Kafka
            kafkaTemplate.send("test-topic", licenseNumber);

            // 2. Print the specific message you requested to the console
            System.out.println("License number " + licenseNumber + " sent to Kafka to fetch RC details.");

            return "Request for License: " + licenseNumber + " has been broadcasted.";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to broadcast to Kafka: " + e.getMessage();
        }
    }

    // Helper: Entity -> DTO (Filtering and Calculating)
    private LicenseDTO convertToDto(License lic) {
        LicenseDTO dto = new LicenseDTO();
        dto.setId(lic.getId());
        dto.setName(lic.getName());
        dto.setFatherName(lic.getFatherName());
        dto.setDob(lic.getDob());

        // Logic: Masking the address like a password for GET requests
         dto.setAddress(lic.getAddress()); // Uncomment this if you WANT to show address

        dto.setLicenseNumber(lic.getLicenseNumber());
        dto.setIssueDate(lic.getIssueDate());
        dto.setBloodGroup(lic.getBloodGroup());
        dto.setVehicleClass(lic.getVehicleClass());

        // Calculate Status: Issue Date + 20 years
        LocalDate issueDate = LocalDate.parse(lic.getIssueDate());
        LocalDate expiryDate = issueDate.plusYears(20);
        dto.setStatus(expiryDate.isBefore(LocalDate.now()) ? "EXPIRED" : "ACTIVE");

        return dto;
    }

    // Helper: DTO -> Entity (Saving data)
    private void mapToEntity(LicenseDTO dto, License entity) {
        entity.setName(dto.getName());
        entity.setId(dto.getId());
        entity.setFatherName(dto.getFatherName());
        entity.setDob(dto.getDob());
        entity.setAddress(dto.getAddress()); // We save the address to DB
        entity.setLicenseNumber(dto.getLicenseNumber());
        entity.setIssueDate(dto.getIssueDate());
        entity.setBloodGroup(dto.getBloodGroup());
        entity.setVehicleClass(dto.getVehicleClass());
    }

    public String save(LicenseDTO dto) {
        if (repo.findByLicenseNumber(dto.getLicenseNumber()).isPresent()) {
            return "License already exists!";
        }
        License lic = new License();
        mapToEntity(dto, lic);
        repo.save(lic);
        return "License saved successfully";
    }

    public List<LicenseDTO> getAll() {
        return repo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public LicenseDTO getByLicenseNumber(String licenseNumber) {
        License lic = repo.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new RuntimeException("License not found"));
        return convertToDto(lic);
    }

    public LicenseDTO getByLicenseId(long id) {
        License lic = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("License not found with id: " + id));
        return convertToDto(lic);
    }

    public String updateByLicenseNumber(String licenseNumber, LicenseDTO updatedDto) {
        License existing = repo.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new RuntimeException("License does not exist"));

        mapToEntity(updatedDto, existing);
        repo.save(existing);
        return "License updated successfully";
    }

    // Delete methods remain similar as they use IDs/Numbers
    public String deleteByLicenseNumber(String licenseNumber) {
        License lic = repo.findByLicenseNumber(licenseNumber)
                .orElseThrow(() -> new RuntimeException("License not found"));
        repo.delete(lic);
        return "License deleted successfully";
    }

    // Delete method using the Primary Key (ID)
    public String deleteById(Long id) {
        // 1. Check if the entity exists, or throw an exception if it doesn't
        License lic = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("License with ID " + id + " not found"));

        // 2. Perform the deletion
        repo.delete(lic);

        // 3. Return a confirmation message
        return "License with ID " + id + " deleted successfully";
    }
}