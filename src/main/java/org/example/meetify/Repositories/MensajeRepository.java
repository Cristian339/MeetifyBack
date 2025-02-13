package org.example.meetify.Repositories;

import org.example.meetify.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByRoomId(String roomId);

    @Query("SELECT m FROM Mensaje m WHERE m.roomId = :roomId ORDER BY m.fechaEnviado DESC, m.horaEnviado DESC")
    Optional<Mensaje> ultimoMensaje(String roomId);
}