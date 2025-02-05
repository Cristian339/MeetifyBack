package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.models.Greeting;
import org.example.meetify.models.HelloMessage;
import org.example.meetify.models.Mensaje;
import org.example.meetify.Services.MensajeService;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.stereotype.Controller;
import org.springframework.web.util.HtmlUtils;

@Controller
@AllArgsConstructor
public class MensajeController {


    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000); // simulated delay
        return new Greeting("Hello, " + HtmlUtils.htmlEscape(message.getName()) + "!");
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

}*/
