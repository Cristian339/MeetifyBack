package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.meetify.Enum.Estado;

@Data
@AllArgsConstructor

public class SeguidorDTO {
    private Integer id;
    private String nombre;
    private Estado estado;
    private String imageUrlPerfil;

}