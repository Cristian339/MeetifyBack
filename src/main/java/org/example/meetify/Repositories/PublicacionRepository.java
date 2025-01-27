package org.example.meetify.Repositories;

import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PublicacionRepository extends JpaRepository<Publicacion, Long> {
}
