package org.example.meetify.Repositories;

import org.example.meetify.Entities.UsuarioCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UsuarioCategoriaRepository extends JpaRepository<UsuarioCategoria, Long>, JpaSpecificationExecutor<UsuarioCategoria> {

}