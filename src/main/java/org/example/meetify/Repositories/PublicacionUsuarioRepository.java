package org.example.meetify.Repositories;

import org.example.meetify.Entities.PublicacionUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface PublicacionUsuarioRepository extends JpaRepository<PublicacionUsuario, Long>, JpaSpecificationExecutor<PublicacionUsuario> {

}