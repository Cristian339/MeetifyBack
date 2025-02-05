package org.example.meetify.Controller;

import lombok.AllArgsConstructor;

import org.example.meetify.DTO.LoginDTO;
import org.example.meetify.DTO.RegistroDTO;
import org.example.meetify.DTO.RespuestaDTO;
import org.example.meetify.Services.UsuarioService;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {


    private final UsuarioService usuarioService;
    private UsuarioService RegisterService;
    private AuthService LogService;



    @PostMapping("/registro")
    public Usuario registro(@RequestBody RegistroDTO registroDTO){
        return RegisterService.registrarUsuario(registroDTO);
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> registro(@RequestBody LoginDTO dto){
        return LogService.iniciarSesion(dto);
    }





}

