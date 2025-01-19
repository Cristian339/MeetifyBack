package org.example.meetify.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    private String nombre;
    private String apellidos;
    private String correoElectronico;
    private Integer puntajeTotal;
    private String genero;
    private String biografia;
    private String pais;

}
