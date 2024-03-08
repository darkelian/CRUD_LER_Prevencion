package com.crud.demo.dtos;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @NotBlank(message = "La cedula es obligatoria")
    private String username;
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String password;
    @NotBlank(message = "El primer nombre es obligatorio")
    @Pattern(regexp = "^[A-Za-z]+$", message = "El primer nombre solo puede contener letras")
    private String firstName;
    private String middleName;
    @NotBlank(message = "El apellido es obligatoria")
    @Pattern(regexp = "^[A-Za-z]+$", message = "El primer apellido solo puede contener letras")
    private String lastName;
    private String secondLastName;
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    @Past(message = "La fecha de nacimiento debe estar en el pasado")
    private LocalDate birthDate;
}