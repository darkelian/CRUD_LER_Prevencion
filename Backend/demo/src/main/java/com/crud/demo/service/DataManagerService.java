package com.crud.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.demo.dtos.RegisterRequest;
import com.crud.demo.dtos.ResponseUsersDto;
import com.crud.demo.dtos.UserProfileDto;
import com.crud.demo.models.Role;
import com.crud.demo.models.User;
import com.crud.demo.models.UserProfile;
import com.crud.demo.repositories.UserProfileRepository;
import com.crud.demo.repositories.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.Data;

@Data
@Service
public class DataManagerService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Transactional
    public User registerUser(RegisterRequest registerDto, Role role) {
        // Suponiendo que el username es la cedula en RegisterRequest
        String cedula = registerDto.getUsername();

        // Asegúrate de que no exista un usuario con la misma cedula/username
        if (userRepository.existsById(cedula)) {
            throw new IllegalStateException("Ya existe un usuario con la cédula/username proporcionada.");
        }
        // Crea y guarda el usuario
        User newUser = User.builder()
                .username(registerDto.getUsername())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(role) // o ADMIN, según la lógica de negocio
                .build();

        newUser = userRepository.save(newUser);

        // Crea y guarda el perfil del usuario
        UserProfile newUserProfile = UserProfile.builder()
                .cedula(registerDto.getUsername())
                .fechaNacimiento(registerDto.getBirthDate())
                .primerNombre(registerDto.getFirstName())
                .segundoNombre(registerDto.getMiddleName())
                .primerApellido(registerDto.getLastName())
                .segundoApellido(registerDto.getSecondLastName())
                .user(newUser)
                .build();

        userProfileRepository.save(newUserProfile);

        return newUser;
    }

    // Método ajustado para devolver una lista de ResponseUsersDto
    public List<ResponseUsersDto> getAllUserProfiles() {
        // Obtener todos los UserProfiles
        List<UserProfile> userProfiles = userProfileRepository.findAll();

        // Convertir a List<ResponseUsersDto> usando .stream() y .map()
        return userProfiles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Método auxiliar para convertir UserProfile a ResponseUsersDto
    private ResponseUsersDto convertToDto(UserProfile userProfile) {
        return new ResponseUsersDto(
                userProfile.getCedula(),
                userProfile.getFechaNacimiento().toString(),
                userProfile.getPrimerNombre(),
                userProfile.getSegundoNombre(),
                userProfile.getPrimerApellido(),
                userProfile.getSegundoApellido());
    }

    // Método para buscar por cédula o nombre
    public List<ResponseUsersDto> findByCedulaOrNombre(String cedula, String nombre) {
        List<UserProfile> userProfiles = userProfileRepository.findByCedulaOrNombre(cedula, nombre);
        return userProfiles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    // Ordenar por fecha de nacimiento
    public List<ResponseUsersDto> findAllUserProfilesOrderedByFechaNacimiento(boolean orden) {
        List<UserProfile> userProfiles = orden ? userProfileRepository.findAllByOrderByFechaNacimientoDesc()
                : userProfileRepository.findAllByOrderByFechaNacimientoAsc();
        return userProfiles.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Transactional
    public boolean deleteUserByCedula(String cedula) {
        // Primero, verifica si existe un usuario con la cédula proporcionada.
        Optional<User> userOptional = userRepository.findById(cedula);
        if (userOptional.isPresent()) {
            // Si existe, elimina primero todos los perfiles de usuario asociados.
            userProfileRepository.deleteByUser(userOptional.get());

            // Luego, elimina el usuario.
            userRepository.deleteById(cedula);
            return true; // Indica que la eliminación fue exitosa.
        } else {
            return false; // No se encontró el usuario, no se realizó la eliminación.
        }
    }

    // Método para actualizar la contraseña
    public boolean updatePassword(String username, String password, String newPassword) {
        // Encuentra el usuario por su username
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        // Verifica que la contraseña antigua coincida
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("La contraseña actual es incorrecta");
        }

        // Codifica la nueva contraseña y la guarda
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        return true; // Retorna true si la actualización fue exitosa
    }

    // actualizar datos
    public UserProfile updateUserProfile(String cedula, UserProfileDto updateUserProfileDto) {
        UserProfile userProfile = userProfileRepository.findById(cedula)
                .orElseThrow(() -> new EntityNotFoundException("UserProfile no encontrado con cédula: " + cedula));

        userProfile.setPrimerNombre(updateUserProfileDto.getFirstName());
        userProfile.setSegundoNombre(updateUserProfileDto.getLastName());
        userProfile.setPrimerApellido(updateUserProfileDto.getMiddleName());
        userProfile.setSegundoApellido(updateUserProfileDto.getSecondLastName());
        userProfile.setFechaNacimiento(updateUserProfileDto.getBirthDate());

        return userProfileRepository.save(userProfile);
    }
    // buscar un usuario
    public UserProfileDto findUserProfileByCedula(String cedula) {
        // Buscar el perfil de usuario por cédula
        UserProfile userProfile = userProfileRepository.findById(cedula)
                .orElseThrow(() -> new EntityNotFoundException("Perfil de usuario no encontrado con la cédula: " + cedula));
    
        // Convertir el UserProfile a UserProfileDto y retornarlo
        return convertToUserProfileDto(userProfile);
    }
    private UserProfileDto convertToUserProfileDto(UserProfile userProfile) {
        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setBirthDate(userProfile.getFechaNacimiento());
        userProfileDto.setFirstName(userProfile.getPrimerNombre());
        userProfileDto.setLastName(userProfile.getSegundoNombre());
        userProfileDto.setMiddleName(userProfile.getPrimerApellido());
        userProfileDto.setSecondLastName(userProfile.getSegundoApellido());
        return userProfileDto;
    }
}
