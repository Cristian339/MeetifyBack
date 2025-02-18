package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReseniasMiasDTO {

    private String imagenUrlPub;
    private String titulo;
    private String nombreUsuario;
    private String imagenUrlUsuario;
    private int estrellas;
    private String motivo;
}
