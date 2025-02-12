package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MensajeDTO {
    private Integer id;
    private String mensaje;
    private Integer idEmisor;
    private Integer idReceptor;
    private String fecha;
}
