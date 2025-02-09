package org.example.meetify.Repositories;

import org.example.meetify.models.Mensaje;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MensajeRepository extends JpaRepository<Mensaje, Integer> {
    List<Mensaje> findByRoomId(String roomId);

    @Query(value = "select distinct(u.usuario_id) from meetify.mensaje m " +
            "join meetify.usuario u on (m.usuario_emisor_id = u.usuario_id and m.usuario_receptor_id = :id ) " +
            "or (m.usuario_receptor_id = u.usuario_id and m.usuario_emisor_id = :id )",
            nativeQuery = true)
    List<Integer> getConversacionesActivas(Integer id);

    @Query(value = "select m from Mensaje m " +
            "join Perfil u on (m.emisor.id = :idPerfil2 and m.receptor.id = :idPerfil1) or " +
            "(m.receptor.id = :idPerfil2 and m.emisor.id = :idPerfil1) order by m.fechaEnviado desc")
    Mensaje getUltimoMensaje(Integer idPerfil1, Integer idPerfil2);

    @Query(value = "SELECT * FROM ( " +
            "    SELECT DISTINCT ON (contacto) " +
            "        CASE " +
            "            WHEN usuario_emisor_id = :idPerfil THEN usuario_receptor_id " +
            "            ELSE usuario_emisor_id " +
            "        END AS contacto, " +
            "        fecha_enviado, " +
            "        contenido " +
            "    FROM meetify.mensaje " +
            "    WHERE usuario_emisor_id = :idPerfil OR usuario_receptor_id = :idPerfil " +
            "    ORDER BY contacto, fecha_enviado DESC " +
            ") sub " +
            "ORDER BY fecha_enviado desc", nativeQuery = true)
    List<Object> getConversaciones(Integer idPerfil);

    @Query("select m from Mensaje m where (m.emisor.id = :idPerfil1 and m.receptor.id = :idPerfil2) or " +
            "(m.emisor.id = :idPerfil2 and m.receptor.id = :idPerfil1) order by m.fechaEnviado asc")
    List<Mensaje> getByEmisorYReceptor(Integer idPerfil1, Integer idPerfil2);
}