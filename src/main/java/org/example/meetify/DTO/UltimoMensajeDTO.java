package org.example.meetify.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UltimoMensajeDTO {
    private String contenido;
    private LocalDate fechaEnviado;
    private LocalTime horaEnviado;
    private int usuarioEmisorId;
    private int usuarioReceptorId;
}
