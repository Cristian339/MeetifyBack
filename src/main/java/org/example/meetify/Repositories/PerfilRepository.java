package org.example.meetify.Repositories;

import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {

    Optional<Perfil> findByCorreoElectronico(String correoElectronico);
    List<Perfil> findByNombreAndApellidosAndCorreoElectronico(String nombre, String apellidos, String correoElectronico);
    Perfil findTopByUsuario(Usuario usuario);
    Optional<Perfil> findByUsuario(Usuario usuario);

}

