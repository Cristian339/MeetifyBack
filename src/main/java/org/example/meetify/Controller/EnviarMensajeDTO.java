package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnviarMensajeDTO {
    private String texto;
    private Integer idReceptor;
    private Integer conversacionId;
}