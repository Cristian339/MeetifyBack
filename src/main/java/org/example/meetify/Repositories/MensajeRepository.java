package org.example.meetify.Repositories;

import org.example.meetify.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByRoomId(int roomId);
}