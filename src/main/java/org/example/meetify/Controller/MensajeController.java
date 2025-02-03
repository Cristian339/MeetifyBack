package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.models.Mensaje;
import org.example.meetify.Services.MensajeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/{roomId}")
    public Mensaje enviarMensaje(@DestinationVariable String roomId, Mensaje mensaje) {
        return mensajeService.guardarMensaje(roomId, mensaje);
    }
}