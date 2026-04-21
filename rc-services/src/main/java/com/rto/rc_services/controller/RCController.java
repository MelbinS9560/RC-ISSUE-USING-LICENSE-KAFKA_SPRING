package com.rto.rc_services.controller;

import com.rto.rc_services.DTO.RCDTO;
import com.rto.rc_services.entity.RC;
import com.rto.rc_services.service.RCService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/rc")
public class RCController {

    @Autowired
    private RCService service;

//    @GetMapping("/kafka-receive")
//    public ResponseEntity<String> getRC()
//    {
//        String response=service.sendMessage(rcdto);
//        return new ResponseEntity<String>(response, HttpStatus.OK);
//    }

    @PostMapping
    public String create(@RequestBody RCDTO rcDto) {
        return service.save(rcDto);
    }

    @GetMapping
    public List<RCDTO> getAll() {
        return service.getAll();
    }


    @GetMapping("/number/{regNo}")
    public RCDTO getByRegistrationNumber(@PathVariable String regNo) {
        return service.getByRegistrationNumber(regNo);
    }
    // GET by ID
    @GetMapping("/id/{id}")
    public RCDTO getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PutMapping("/{regNo}")
    public String update(@PathVariable String regNo, @RequestBody RCDTO rcDto) {
        return service.updateByRegistrationNumber(regNo, rcDto);
    }

    //  Delete by ID
    @DeleteMapping("/id/{id}")
    public String delete(@PathVariable Long id) {
        return service.delete(id);
    }

    //DELETE BY REGISTRATION NUMBER
    @DeleteMapping("/number/{regNo}")
    public String deleteByRegistrationNumber(@PathVariable String regNo) {
        return service.deleteByRegistrationNumber(regNo);
    }
}