package org.example.meetify.Repositories;

import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Reputacion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReputacionRepository extends JpaRepository<Reputacion, Double> {

    List<Reputacion> findByPublicacion(Publicacion publicacion);

}