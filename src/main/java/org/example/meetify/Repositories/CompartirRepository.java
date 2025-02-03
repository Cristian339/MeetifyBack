package org.example.meetify.Repositories;

import org.example.meetify.models.Compartir;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompartirRepository extends JpaRepository<Compartir, Integer> {

    // Corregido para usar la propiedad correcta en la consulta
    List<Compartir> findByPerfil_Id(Integer perfilId);
}
