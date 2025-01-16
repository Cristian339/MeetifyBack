package dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegistroDTO {

    //TABLA PERFIL
    private  String nombre;
    private String apellidos;
    private String fechaNacimiento;
    private String mail;
    private String dni;

    // TABLA USUARIO
    private String username;
    private String password;

}
