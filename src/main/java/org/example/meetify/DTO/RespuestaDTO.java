package org.example.meetify.DTO;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RespuestaDTO {
    private Integer estado;
    private String token;
    private String rol;
    private String mensaje;
    private Object cuerpo;
}
