package org.example.meetify.Services;

import org.example.meetify.Repositories.MensajeRepository;
import org.example.meetify.models.Mensaje;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public Mensaje guardarMensaje(String roomId, Mensaje mensaje) {
        mensaje.setRoomId(Integer.parseInt(roomId));
        mensaje.setEnviadoEn(LocalDateTime.now());
        return mensajeRepository.save(mensaje);
    }

    public List<Mensaje> obtenerMensajesPorRoomId(String roomId) {
        return mensajeRepository.findByRoomId(Integer.parseInt(roomId));
    }
}