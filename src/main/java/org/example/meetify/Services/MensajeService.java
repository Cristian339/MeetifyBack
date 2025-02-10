package org.example.meetify.Services;

import org.example.meetify.Controller.EnviarMensajeDTO;
import org.example.meetify.DTO.*;
import org.example.meetify.Repositories.MensajeRepository;
import org.example.meetify.Repositories.ConversacionRepository;
import org.example.meetify.models.Mensaje;
import org.example.meetify.models.Conversacion;
import org.example.meetify.models.Perfil;
import org.example.meetify.seguridad.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import lombok.AllArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MensajeService {

    private final MensajeRepository mensajeRepository;
    private final ConversacionRepository conversacionRepository;
    private final PerfilService perfilService;
    private final SimpMessagingTemplate messagingTemplate;

    public List<ConversacionDTO> getConverdacionesByIdPerfil(Integer idPerfil) {
        List<Integer> perfilesConversaciones = mensajeRepository.getConversacionesActivas(idPerfil);
        List<ConversacionDTO> conversacionDTOS = new ArrayList<>();

        for (Integer id : perfilesConversaciones) {
            Perfil p = perfilService.getById(id);
            Mensaje ultimoMensaje = mensajeRepository.getUltimoMensaje(idPerfil, p.getId());

            ConversacionDTO dto = new ConversacionDTO();
            dto.setNombrePerfil(p.getNombre().concat(" ").concat(p.getApellidos()));
            dto.setFotoPerfil(p.getImagenUrlPerfil());
            dto.setUltimoMensaje(ultimoMensaje.getContenido());
            dto.setFechaUltimoMensaje(ultimoMensaje.getFechaEnviado().atStartOfDay());
            conversacionDTOS.add(dto);
        }

        return conversacionDTOS;
    }

    public List<ChatDBDTO> getConversaciones(Perfil perfil) {
        List<Object> cosas = mensajeRepository.getConversaciones(perfil.getId());
        List<ChatDBDTO> chats = new ArrayList<>();
        for (Object o : cosas) {
            Object[] oa = (Object[]) o;
            Timestamp tm = (Timestamp) oa[1];
            PerfilDTO perfilDTO = perfilService.getPerfilDTOPorId((Integer) oa[0]);
            chats.add(new ChatDBDTO((Integer) oa[0], tm.toLocalDateTime(), (String) oa[2], perfilDTO));
        }

        return chats;
    }

    public List<MensajeDTO> getByEmisorReceptor(Integer idPerfil1, Integer idPerfil2) {
        List<MensajeDTO> dtos = new ArrayList<>();
        mensajeRepository.getByEmisorYReceptor(idPerfil1, idPerfil2).forEach(
                m -> dtos.add(new MensajeDTO(m.getId(), m.getContenido(),
                        m.getEmisor().getId(), m.getReceptor().getId(), m.getFechaEnviado().toString()))
        );

        return dtos;
    }

    public RespuestaDTO enviarMensaje(Perfil perfil, EnviarMensajeDTO enviarMensajeDTO) {
        Conversacion conversacion = conversacionRepository.findById(enviarMensajeDTO.getConversacionId())
                .orElseThrow(() -> new IllegalArgumentException("Conversacion no encontrada"));

        Mensaje mensaje = new Mensaje();
        mensaje.setFechaEnviado(LocalDate.now());
        mensaje.setHoraEnviado(LocalTime.now());
        mensaje.setEmisor(perfil);
        mensaje.setReceptor(perfilService.getById(enviarMensajeDTO.getIdReceptor()));
        mensaje.setContenido(enviarMensajeDTO.getTexto());
        mensaje.setConversacion(conversacion);

        Mensaje savedMensaje = mensajeRepository.save(mensaje);
        messagingTemplate.convertAndSend("/topic/mensajes", savedMensaje);

        return RespuestaDTO.builder().estado(200).mensaje("Mensaje Enviado").build();
    }

    public Mensaje guardarMensaje(Mensaje mensaje) {
        if (mensaje.getEmisor() == null) {
            throw new IllegalArgumentException("El emisor del mensaje no puede ser nulo");
        }
        if (mensaje.getReceptor() == null) {
            throw new IllegalArgumentException("El receptor del mensaje no puede ser nulo");
        }
        if (mensaje.getConversacion() == null) {
            throw new IllegalArgumentException("La conversacion no puede ser nula");
        }
        mensaje.setFechaEnviado(LocalDate.now());
        mensaje.setHoraEnviado(LocalTime.now());
        return mensajeRepository.save(mensaje);
    }

    public Conversacion crearConversacion(Integer usuario1Id, Integer usuario2Id) {
        Perfil usuario1 = perfilService.getById(usuario1Id);
        Perfil usuario2 = perfilService.getById(usuario2Id);
        Conversacion conversacion = new Conversacion();
        conversacion.setUsuario1(usuario1);
        conversacion.setUsuario2(usuario2);
        return conversacionRepository.save(conversacion);
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

    public List<Mensaje> obtenerMensajesPorConversacionId(Integer conversacionId) {
        Conversacion conversacion = conversacionRepository.findById(conversacionId)
                .orElseThrow(() -> new IllegalArgumentException("Conversacion no encontrada"));
        return mensajeRepository.findByConversacion(conversacion);
    }


}