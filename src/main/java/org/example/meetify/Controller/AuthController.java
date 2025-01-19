package org.example.meetify.Controller;

import org.example.meetify.Entities.Usuario;
import org.example.meetify.Services.UsuarioService;
import org.example.meetify.dto.LoginDTO;
import org.example.meetify.dto.RegistroDTO;
import org.example.meetify.dto.RespuestaDTO;
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

    @PostMapping("/registro/perfil")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return service.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return service.iniciarSesion(dto);
    }
}
