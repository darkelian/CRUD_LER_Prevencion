package com.crud.demo.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordDto {
    /* @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") */
    private String oldPassword;
    /* @NotBlank
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres") */
    private String newPassword;
}
