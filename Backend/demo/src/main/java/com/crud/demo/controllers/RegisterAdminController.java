package com.crud.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.dtos.RegisterRequest;
import com.crud.demo.dtos.StandardResponseDTO;
import com.crud.demo.models.Role;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.security.JwtService;
import com.crud.demo.service.DataManagerService;

import jakarta.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/manager")
public class RegisterAdminController {
    private final DataManagerService dataManagerService;
    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtService jwtService;

    public RegisterAdminController(DataManagerService dataManagerService,
            JwtAuthenticationFilter authenticationFilter, JwtService jwtService) {
        this.dataManagerService = dataManagerService;
        this.authenticationFilter = authenticationFilter;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<StandardResponseDTO> postRegister(@Validated @RequestBody RegisterRequest request) {
        try {
            dataManagerService.registerUser(request, Role.USER);
            return ResponseEntity
                    .ok(new StandardResponseDTO().fullSuccess("Usuario" + request.getUsername() + " registrado"));
        } catch (Exception ex) {
            return ResponseEntity
                    .ok(new StandardResponseDTO().failSuccess("no se pudo registrar usuario " + request.getUsername()));
        }
    }

    @DeleteMapping("/")
    public ResponseEntity<StandardResponseDTO> deleteUser(@RequestParam String username) {
        return ResponseEntity.ok(
                dataManagerService.deleteUserByCedula(username)
                        ? new StandardResponseDTO().fullSuccess("Usuario eliminado")
                        : new StandardResponseDTO().failSuccess("Usuario no encontrado"));
    }
}
