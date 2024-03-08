package com.crud.demo.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.dtos.ResponseUsersDto;
import com.crud.demo.dtos.StandardResponseDTO;
import com.crud.demo.dtos.UpdatePasswordDto;
import com.crud.demo.dtos.UserProfileDto;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.security.JwtService;
import com.crud.demo.service.DataManagerService;

import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/utils")
public class DataController {
    private final DataManagerService dataManagerService;
    private final JwtAuthenticationFilter authenticationFilter;
    private final JwtService jwtService;

    public DataController(DataManagerService dataManagerService, JwtAuthenticationFilter authenticationFilter,
            JwtService jwtService) {
        this.dataManagerService = dataManagerService;
        this.authenticationFilter = authenticationFilter;
        this.jwtService = jwtService;
    }

    @GetMapping()
    public ResponseEntity<StandardResponseDTO> getAllUsers() {
        try {
            return ResponseEntity
                    .ok(new StandardResponseDTO().fullSuccess(dataManagerService.getAllUserProfiles()));
        } catch (Exception ex) {
            return ResponseEntity
                    .ok(new StandardResponseDTO().failSuccess("No se pudo cargar los usuarios"));
        }
    }

    @GetMapping("/user")
    public ResponseEntity<StandardResponseDTO> getUserByUsername(@RequestParam String username) {
        try {
            return ResponseEntity
                    .ok(new StandardResponseDTO().fullSuccess(dataManagerService.findUserProfileByCedula(username)));
        } catch (Exception ex) {
            return ResponseEntity
                    .ok(new StandardResponseDTO().failSuccess("No se pudo cargar los usuarios"));
        }
    }

    @GetMapping("/category")
    public ResponseEntity<StandardResponseDTO> getUsersByCategory(@RequestParam String category) {
        List<ResponseUsersDto> users = dataManagerService.findByCedulaOrNombre(category, category);
        if (users.isEmpty()) {
            return ResponseEntity.ok(new StandardResponseDTO().failSuccess("No se encontraron datos"));
        }
        return ResponseEntity.ok(new StandardResponseDTO().fullSuccess(users));
    }

    @GetMapping("orden")
    public ResponseEntity<StandardResponseDTO> findByOrdenDescend(@RequestParam boolean orden) {
        try {
            return ResponseEntity
                    .ok(new StandardResponseDTO()
                            .fullSuccess(dataManagerService.findAllUserProfilesOrderedByFechaNacimiento(orden)));
        } catch (Exception ex) {
            return ResponseEntity
                    .ok(new StandardResponseDTO().failSuccess("No se pudo cargar los usuarios"));
        }
    }

    @PatchMapping("/password")
    public ResponseEntity<StandardResponseDTO> updatePassword(
            /* @Validated */ @RequestBody UpdatePasswordDto passwordDto,
            HttpServletRequest request) {
        String token = authenticationFilter.getTokenFromRequest(request);
        String usertoken = jwtService.getUsernameFromToken(token);
        dataManagerService.updatePassword(usertoken, passwordDto.getOldPassword(), passwordDto.getNewPassword());
        return ResponseEntity.ok(new StandardResponseDTO().fullSuccess("Contraseña actualizada"));
    }

    @PutMapping("/users")
    public ResponseEntity<StandardResponseDTO> updateUserProfile(@RequestBody UserProfileDto updateUserProfileDto,
            @RequestParam String username) {
        dataManagerService.updateUserProfile(username, updateUserProfileDto);
        return ResponseEntity.ok(new StandardResponseDTO().fullSuccess("Perfil actualizado con éxito"));
    }
}
