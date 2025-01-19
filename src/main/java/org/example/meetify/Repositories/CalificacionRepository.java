package org.example.meetify.Repositories;

import org.example.meetify.Entities.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CalificacionRepository extends JpaRepository<Calificacion, Long>, JpaSpecificationExecutor<Calificacion> {

}