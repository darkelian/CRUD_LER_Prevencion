package com.crud.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.dtos.RegisterRequest;
import com.crud.demo.dtos.StandardResponseDTO;
import com.crud.demo.models.Role;
import com.crud.demo.service.DataManagerService;

import java.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    private final DataManagerService dataManagerService;

    public ConfigController(DataManagerService dataManagerService) {
        this.dataManagerService = dataManagerService;
    }

    @PostMapping("/init")
    public ResponseEntity<StandardResponseDTO> initDatabases() {
        try {
            LocalDate date = LocalDate.of(2024, 3, 7);
            RegisterRequest request = new RegisterRequest("1234", "admin1234", "admin", "", "admin", "", date);
            dataManagerService.registerUser(request, Role.ADMIN);
            return ResponseEntity.ok(new StandardResponseDTO().fullSuccess("administrador registrado"));
        } catch (Exception ex) {
            return ResponseEntity.ok(new StandardResponseDTO().failSuccess("Usuario admin ya esta"));
        }

    }

}
