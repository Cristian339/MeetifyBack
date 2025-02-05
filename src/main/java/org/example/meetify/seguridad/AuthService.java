package org.example.meetify.seguridad;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.LoginDTO;
import org.example.meetify.DTO.RespuestaDTO;
import org.example.meetify.Repositories.UsuarioRepository;
import org.example.meetify.Services.UsuarioService;
import org.example.meetify.models.Usuario;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {
    private UsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private JWTService jwtService;
    private UsuarioService usuarioService;

    public ResponseEntity<RespuestaDTO> iniciarSesion(LoginDTO dto){

        // Buscar usuario por nombre de usuario
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByNombreUsuario(dto.getNombreUsuario());

        return getRespuestaDTOResponseEntity(dto, usuarioOpcional, passwordEncoder, jwtService);

    }

    public static ResponseEntity<RespuestaDTO> getRespuestaDTOResponseEntity(LoginDTO dto, Optional<Usuario> usuarioOpcional, PasswordEncoder passwordEncoder, JWTService jwtService) {
        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            String rol = usuario.getRol().toString();  // Obtener el rol del usuario
            // Verificar la contraseña
            if (passwordEncoder.matches(dto.getContrasenia(), usuario.getPassword())) {

                // Contraseña válida, devolver token de acceso
                String token = jwtService.generateToken(usuario);

                // Devolver RespuestaDTO con el rol incluido
                return ResponseEntity
                        .ok(RespuestaDTO
                                .builder()
                                .estado(HttpStatus.OK.value())
                                .token(token)
                                .rol(rol)
                                .build());
            } else {
                throw new BadCredentialsException("Contraseña incorrecta");
            }
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }


}
