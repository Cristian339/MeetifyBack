package org.example.meetify.DTO;

import lombok.Builder;
import lombok.Data;
import org.example.meetify.Enum.Genero;

@Data
@Builder
public class ActualizarBiografiaDTO {
    private String pais;
    private Genero genero;
    private String imagenUrl;
    private String biografia;
}