package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class MiPuntuacionDTO {
    private String nombreUsuario;
    private String imagenUrlPub;
    private String imagenUrlPerfil;
    private int estrellas;
    private String titulo;
    private String categoria;

}
