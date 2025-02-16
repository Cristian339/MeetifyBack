package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AmigoDTO {
    private Integer id;
    private String nombre;
    private String apellidos;
    private String imagenUrlPerfil;
}
