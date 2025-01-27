package org.example.meetify.Repositories;

import org.example.meetify.models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

    Optional<Perfil> findByCorreoElectronico(String correoElectronico);
}
