package org.example.meetify.seguridad;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TokenDataDTO {
    private String username;
    private String rol;
    private long fecha_creacion;
    private long fecha_expiracion;


    public long getFechaExpiracion() {
        return fecha_expiracion;
    }

    public void setFechaExpiracion(long fecha_expiracion) {
        this.fecha_expiracion = fecha_expiracion;
    }
}