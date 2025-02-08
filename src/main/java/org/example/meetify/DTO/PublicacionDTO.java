package org.example.meetify.DTO;

import lombok.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionDTO {

    private String nombrePerfil;
    private String categoria;
    private String imageUrlPub;
    private String imageUrlPerfil;
    private String titulo;
    private String descripcion;
    private String ubicacion;
    private LocalDate fechaIni;
    private LocalDate fechaFin;


}