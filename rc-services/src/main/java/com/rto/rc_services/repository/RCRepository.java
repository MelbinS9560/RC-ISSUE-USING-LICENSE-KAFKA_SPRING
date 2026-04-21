package com.rto.rc_services.repository;

import com.rto.rc_services.entity.RC;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

    public interface RCRepository extends JpaRepository<RC, Long> {
        Optional<RC> findByRegistrationNumber(String registrationNumber);


        List<RC> findByLicenseNumberAndVehicleType(String licenseNumber, String vehicleType);
    }