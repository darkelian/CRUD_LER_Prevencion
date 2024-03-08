package com.crud.demo.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.dtos.LoginRequest;
import com.crud.demo.dtos.StandardResponseDTO;
import com.crud.demo.service.AuthResponse;
import com.crud.demo.service.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<StandardResponseDTO> login(@Validated @RequestBody LoginRequest request) {
        try {
            AuthResponse authResponse = authService.login(request);
            StandardResponseDTO successResponse = new StandardResponseDTO().fullSuccess(authResponse);
            return ResponseEntity.ok(successResponse);
        } catch (Exception ex) {
            return ResponseEntity.ok(new StandardResponseDTO().failSuccess("Credenciales invalidas"));
        }
    }
}