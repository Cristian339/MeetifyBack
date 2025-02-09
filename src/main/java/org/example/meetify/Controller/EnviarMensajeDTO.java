package org.example.meetify.Controller;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EnviarMensajeDTO {


    private String texto;
    private Integer idReceptor;
}
