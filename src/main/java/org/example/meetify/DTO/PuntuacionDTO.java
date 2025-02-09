package org.example.meetify.DTO;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PuntuacionDTO {

    private Integer id;
    private String nombreUsuario;
    private Double puntuacion;
}