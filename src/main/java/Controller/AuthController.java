package Controller;

import Entities.Usuario;
import Services.UsuarioService;
import dto.LoginDTO;
import dto.RegistroDTO;
import dto.RespuestaDTO;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController  {

    private UsuarioService service;

    @PostMapping("/registro")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return service.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return service.iniciarSesion(dto);
    }
}
