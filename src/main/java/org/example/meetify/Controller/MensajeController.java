package org.example.meetify.Controller;

import org.example.meetify.Services.MensajeService;
import org.example.meetify.models.Mensaje;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import lombok.AllArgsConstructor;

import java.util.List;

@Controller
@AllArgsConstructor
public class MensajeController {

    private final MensajeService mensajeService;

    @MessageMapping("/enviarMensaje")
    @SendTo("/topic/mensajes")
    public Mensaje enviarMensaje(Mensaje mensaje) throws Exception {
        return mensajeService.guardarMensaje(mensaje);
    }

    @GetMapping("/mensajes/{roomId}")
    public List<Mensaje> obtenerMensajesPorRoomId(@PathVariable String roomId) {
        return mensajeService.obtenerMensajesPorRoomId(roomId);
    }
}


/*
public class MensajeController {

    private final MensajeService mensajeService;

    @MessageMapping("/test")
    @SendTo("/tema/test")

    public Mensaje enviarMensaje( Mensaje mensaje) throws Exception {
        return mensajeService.guardarMensaje("1", mensaje);
    }
    */
/*@DestinationVariable*//*


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
    }
}*/
