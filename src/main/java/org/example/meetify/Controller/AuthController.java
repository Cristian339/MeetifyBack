
package org.example.meetify.Controller;

import lombok.AllArgsConstructor;

import org.example.meetify.DTO.LoginDTO;
import org.example.meetify.DTO.RegistroDTO;
import org.example.meetify.DTO.RespuestaDTO;
import org.example.meetify.Services.UsuarioService;
import org.example.meetify.models.Usuario;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


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

