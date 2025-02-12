package org.example.meetify.Controller;

import jakarta.mail.MessagingException;
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
    private final AuthService authService;

    @PostMapping("/registro")
    public ResponseEntity<String> registro(@RequestBody RegistroDTO registroDTO) {
        usuarioService.registrarUsuario(registroDTO);
        return ResponseEntity.ok("Registro exitoso. Revisa tu correo para verificar tu cuenta.");
    }

    @PostMapping("/login")
    public ResponseEntity<RespuestaDTO> login(@RequestBody LoginDTO dto) {
        return authService.iniciarSesion(dto);
    }
}