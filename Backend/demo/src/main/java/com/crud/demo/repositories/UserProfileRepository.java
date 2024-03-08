package com.crud.demo.repositories;

import com.crud.demo.models.User;
import com.crud.demo.models.UserProfile;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserProfileRepository extends JpaRepository<UserProfile, String> {
    @Query("SELECT up FROM UserProfile up WHERE up.cedula = :cedula OR lower(up.primerNombre) LIKE lower(concat('%', :nombre, '%')) OR lower(up.segundoNombre) LIKE lower(concat('%', :nombre, '%'))")
    List<UserProfile> findByCedulaOrNombre(@Param("cedula") String cedula, @Param("nombre") String primerNombre);

    List<UserProfile> findAllByOrderByFechaNacimientoDesc();

    List<UserProfile> findAllByOrderByFechaNacimientoAsc();

    void deleteByUser(User user);

}
