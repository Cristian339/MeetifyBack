package org.example.meetify.Services;

import org.example.meetify.Repositories.MensajeRepository;
import org.example.meetify.models.Mensaje;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
@AllArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;

    public Mensaje guardarMensaje(String roomId, Mensaje mensaje) {
        mensaje.setRoomId(Integer.parseInt(roomId));
        mensaje.setFechaEnviado(LocalDate.now());
        mensaje.setHoraEnviado(LocalTime.now());
        return mensajeRepository.save(mensaje);
    }

    public Mensaje editarMensaje(Integer mensajeId, Mensaje mensajeActualizado) {
        Mensaje mensajeExistente = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
        mensajeExistente.setContenido(mensajeActualizado.getContenido());
        mensajeExistente.setFechaEnviado(LocalDate.now());
        mensajeExistente.setHoraEnviado(LocalTime.now());
        return mensajeRepository.save(mensajeExistente);
    }

    public void eliminarMensaje(Integer mensajeId) {
        Mensaje mensajeExistente = mensajeRepository.findById(mensajeId)
                .orElseThrow(() -> new IllegalArgumentException("Mensaje no encontrado"));
        mensajeRepository.delete(mensajeExistente);
    }

    public List<Mensaje> obtenerMensajesPorRoomId(String roomId) {
        return mensajeRepository.findByRoomId(Integer.parseInt(roomId));
    }
}