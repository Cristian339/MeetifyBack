package org.example.meetify.Controller;

import org.example.meetify.DTO.*;
import org.example.meetify.Services.MensajeService;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.models.Conversacion;
import org.example.meetify.models.Mensaje;
import org.example.meetify.models.Perfil;
import org.example.meetify.seguridad.JWTService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import lombok.AllArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/chat")
@AllArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;
    private final PerfilService perfilService;
    private final JWTService jwtService;


    @MessageMapping("/enviarMensaje")
    @SendTo("/topic/mensajes")
    public Mensaje enviarMensaje(Mensaje mensaje) throws Exception {
        return mensajeService.guardarMensaje(mensaje);
    }

    @GetMapping("/conversaciones/{idPerfil}")
    public List<ConversacionDTO> getConversacionesByIdPerfil(@PathVariable Integer idPerfil) {
        return mensajeService.getConverdacionesByIdPerfil(idPerfil);
    }

    @GetMapping("/conversacion/{conversacionId}")
    public List<Mensaje> obtenerMensajesPorConversacionId(@PathVariable Integer conversacionId) {
        return mensajeService.obtenerMensajesPorConversacionId(conversacionId);
    }

/*
    @PostMapping("/compartir/{id}")
    public PublicacionDTO compartirPublicacion(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        Perfil perfil = jwtService.extraerPerfilToken(token);
        System.out.println(perfil.getCorreoElectronico());
        return compartirService.compartirPublicacion(id,perfil);
    }
*/

    @PostMapping("/crear-conversacion")
    public Conversacion crearConversacion(@RequestHeader("Authorization") String token, @RequestParam Integer usuario2Id) {
        Perfil miUsuario = jwtService.extraerPerfilToken(token);
        return mensajeService.crearConversacion(miUsuario.getId(), usuario2Id);
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