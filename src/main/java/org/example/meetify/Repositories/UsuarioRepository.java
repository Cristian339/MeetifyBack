package org.example.meetify.Repositories;

import org.example.meetify.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findTopByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findTopByCorreoElectronico(String correoElectronico);
}