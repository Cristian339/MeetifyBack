package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReputacionDTO {
    private String nombreUsuario;
    private String imagenUrlUsuario;
    private int estrellas;
    private String motivo;
}
