package org.example.meetify.Controller;

import org.example.meetify.DTO.ChatDBDTO;
import org.example.meetify.DTO.ConversacionDTO;
import org.example.meetify.DTO.MensajeDTO;
import org.example.meetify.DTO.RespuestaDTO;
import org.example.meetify.Services.MensajeService;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.models.Mensaje;
import org.example.meetify.models.Perfil;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/mensajes")
@AllArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;
    private final PerfilService perfilService;

    @MessageMapping("/enviarMensaje")
    @SendTo("/topic/mensajes")
    public Mensaje enviarMensaje(Mensaje mensaje) throws Exception {
        return mensajeService.guardarMensaje(mensaje);
    }

    @GetMapping("/room/{roomId}")
    public List<Mensaje> obtenerMensajesPorRoomId(@PathVariable String roomId) {
        return mensajeService.obtenerMensajesPorRoomId(roomId);
    }

    @GetMapping("/conversaciones/{idPerfil}")
    public List<ConversacionDTO> getConversacionesByIdPerfil(@PathVariable Integer idPerfil) {
        return mensajeService.getConverdacionesByIdPerfil(idPerfil);
    }

    @GetMapping("/conversaciones")
    public List<ChatDBDTO> getConversaciones(@RequestParam Integer idPerfil) {
        Perfil perfil = perfilService.getById(idPerfil);
        return mensajeService.getConversaciones(perfil);
    }

    @GetMapping("/emisor-receptor")
    public List<MensajeDTO> getByEmisorReceptor(@RequestParam Integer idPerfil1, @RequestParam Integer idPerfil2) {
        return mensajeService.getByEmisorReceptor(idPerfil1, idPerfil2);
    }

    @PostMapping("/enviar")
    public RespuestaDTO enviarMensaje(@RequestBody EnviarMensajeDTO enviarMensajeDTO, @RequestParam Integer idPerfil) {
        Perfil perfil = perfilService.getById(idPerfil);
        return mensajeService.enviarMensaje(perfil, enviarMensajeDTO);
    }

    @PutMapping("/editar/{mensajeId}")
    public Mensaje editarMensaje(@PathVariable Integer mensajeId, @RequestBody Mensaje mensajeActualizado) {
        return mensajeService.editarMensaje(mensajeId, mensajeActualizado);
    }

    @DeleteMapping("/eliminar/{mensajeId}")
    public void eliminarMensaje(@PathVariable Integer mensajeId) {
        mensajeService.eliminarMensaje(mensajeId);
    }
}


/*
    @MessageMapping("/enviarMensaje")
    @SendTo("/topic/mensajes")
    public Mensaje enviarMensaje(Mensaje mensaje) throws Exception {
        return mensajeService.guardarMensaje(mensaje);
    }

    @GetMapping("/mensajes/{roomId}")
    public List<Mensaje> obtenerMensajesPorRoomId(@PathVariable String roomId) {
        return mensajeService.obtenerMensajesPorRoomId(roomId);
    }
}*/
