package org.example.meetify.dto;

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
    private String dni;

    // TABLA USUARIO
    private String nombreUsuario;
    private String contrasenia;

}
