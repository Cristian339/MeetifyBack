package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.meetify.Enum.Genero;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PerfilDTO {
    private String nombre;
    private String apellidos;
    private String correoElectronico;
    private Integer puntajeTotal;
    private Genero genero;
    private String biografia;
    private String pais;

}
