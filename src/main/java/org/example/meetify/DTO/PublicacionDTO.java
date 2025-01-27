package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PublicacionDTO {

    private String nombrePerfil;
    private String categoria;
    private String imageUrl;
    private String titulo;
    private String descripcion;
    private String ubicacion;



}
