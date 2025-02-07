package org.example.meetify.Repositories;

import org.example.meetify.models.Publicacion;
import org.example.meetify.models.PublicacionPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface publicacionPerfilRepository extends JpaRepository<PublicacionPerfil, Integer> {

    List<PublicacionPerfil> findByPublicacion(Publicacion publicacion);
}