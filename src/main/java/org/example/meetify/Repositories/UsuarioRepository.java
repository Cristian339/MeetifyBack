package org.example.meetify.Repositories;

import org.example.meetify.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findTopByNombreUsuario(String nombreUsuario);
    Optional<Usuario> findTopByCorreoElectronico(String correoElectronico);
}