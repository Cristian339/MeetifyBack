package org.example.meetify.Repositories;

import org.example.meetify.Entities.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PerfilRepository extends JpaRepository<Perfil, Integer> {
    // Método para buscar perfiles por nombre, apellidos y correo electrónico
    List<Perfil> findByNombreAndApellidosAndCorreoElectronico(String nombre, String apellidos, String correoElectronico);
}
