package com.rto.license_services.repository;

import java.util.Optional;

import com.rto.license_services.entity.License;
import org.springframework.data.jpa.repository.JpaRepository;


public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByLicenseNumber(String licenseNumber);

}