package org.example.meetify.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroDTO {

    //TABLA PERFIL
    private  String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String correoElectronico;

    // TABLA USUARIO
    private String nombreUsuario;
    private String contrasenia;

}
