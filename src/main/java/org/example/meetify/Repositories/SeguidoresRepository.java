package org.example.meetify.Repositories;

import org.example.meetify.DTO.SeguidorDTO;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Seguidores;
import org.example.meetify.Enum.Estado;
import org.example.meetify.models.SeguidoresId;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SeguidoresRepository extends JpaRepository<Seguidores, SeguidoresId> {
    List<Seguidores> findBySeguidorAndEstado(Perfil seguidor, Estado estado);
    Seguidores findBySeguidorAndSeguido(Perfil seguidor, Perfil seguido);
    boolean existsBySeguidorAndSeguido(Perfil seguidor, Perfil seguido);
}