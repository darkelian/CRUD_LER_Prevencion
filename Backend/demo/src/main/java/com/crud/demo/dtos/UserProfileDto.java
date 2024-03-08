package com.crud.demo.dtos;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UserProfileDto {
    private String firstName;
    private String lastName;
    private String middleName;
    private String secondLastName;
    private LocalDate birthDate;
}
