package com.rto.license_services.controller;

import com.rto.license_services.DTO.LicenseDTO;
import com.rto.license_services.entity.License;
import com.rto.license_services.service.LicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/license")
public class LicenseController {

    @Autowired
    private LicenseService service;

//    @PostMapping("/kafka-send")
//    public ResponseEntity<String> addLicense(@RequestBody LicenseDTO dto)
//    {
//        String response=service.sendMessage(dto);
//        return new ResponseEntity<String>(response, HttpStatus.OK);
//    }

    // REMOVE or Comment out the local KafkaTemplate<String, String>

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @GetMapping("/issue-rc/{licenseNumber}")
    public String triggerRC(@PathVariable String licenseNumber) {
        // Just send the raw string. No DB check, no DTO.
        kafkaTemplate.send("test-topic", licenseNumber);

        System.out.println("License number " + licenseNumber + " sent to Kafka to fetch RC details.");

        return "Request for License: " + licenseNumber + " has been broadcasted.";
    }




    @PostMapping
    public String create(@RequestBody LicenseDTO licenseDto) {
        return service.save(licenseDto);
    }

    @GetMapping("/number/{licenseNumber}")
    public LicenseDTO getByLicenseNumber(@PathVariable String licenseNumber) {
        return service.getByLicenseNumber(licenseNumber);
    }

    @GetMapping("/id/{id}")
    public LicenseDTO getById(@PathVariable long id) {
        return service.getByLicenseId(id);
    }

    @GetMapping
    public List<LicenseDTO> getAll() {
        return service.getAll();
    }

    @PutMapping("/number/{licenseNumber}")
    public String update(@PathVariable String licenseNumber, @RequestBody LicenseDTO dto) {
        return service.updateByLicenseNumber(licenseNumber, dto);
    }

    // DELETE BY ID
    @DeleteMapping("/id/{id}")
    public String deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }

    // DELETE BY LICENSE NUMBER
    @DeleteMapping("/number/{licenseNumber}")
    public String deleteByNumber(@PathVariable String licenseNumber) {
        return service.deleteByLicenseNumber(licenseNumber);
    }


}