package org.example.meetify.Repositories;

import org.example.meetify.models.Reputacion;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Perfil;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReputacionRepository extends JpaRepository<Reputacion, Integer> {
    boolean existePorPublicacionYPerfil(Publicacion publicacion, Perfil perfil);
}