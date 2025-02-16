package org.example.meetify.Repositories;

import org.example.meetify.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByRoomId(String roomId);

    @Query(value = "SELECT * FROM mensaje WHERE room_id = :room_id ORDER BY fecha_enviado DESC, hora_enviado DESC LIMIT 1", nativeQuery = true)
    Optional<Mensaje> ultimoMensaje(@Param("room_id") String roomId);



    /*
    * SELECT * FROM mensaje
WHERE room_id = :room_id
ORDER BY fecha_enviado DESC, hora_enviado DESC
LIMIT 1;



    *
    * */
}