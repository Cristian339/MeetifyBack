package org.example.meetify.Repositories;

import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.PublicacionPerfil;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface publicacionPerfilRepository extends JpaRepository<PublicacionPerfil, Integer> {

    List<PublicacionPerfil> findByPublicacion(Publicacion publicacion);
    Optional<PublicacionPerfil> findByPerfilAndPublicacion(Perfil perfil, Publicacion publicacion);


}