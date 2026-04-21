package com.rto.rc_services.kafka;

import com.rto.rc_services.repository.RCRepository;
import com.rto.rc_services.service.RCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
@Service
public class RCListenerService {

    @Autowired
    private RCService rcService; // Inject the Service, not the Repo directly

    @KafkaListener(topics = "test-topic", groupId = "two-wheeler-group")
    public void consumeTwoWheeler(String licenseNumber) {
        // REMOVE QUOTES: This turns "\"KA51...\"" into "KA51..."
        String cleanId = licenseNumber.replace("\"", "");

        rcService.validateRCByVehicleType(cleanId, "2-WHEELER");
    }

    @KafkaListener(topics = "test-topic", groupId = "four-wheeler-group")
    public void consumeFourWheeler(String licenseNumber) {
        String cleanId = licenseNumber.replace("\"", "");
        rcService.validateRCByVehicleType(cleanId, "4-WHEELER");
    }

    @KafkaListener(topics = "test-topic", groupId = "heavy-group")
    public void consumeHeavy(String licenseNumber) {
        String cleanId = licenseNumber.replace("\"", "");
        rcService.validateRCByVehicleType(cleanId, "HEAVY");
    }

}