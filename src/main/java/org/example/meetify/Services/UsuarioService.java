package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.LoginDTO;
import org.example.meetify.DTO.RegistroDTO;
import org.example.meetify.DTO.RespuestaDTO;
import org.example.meetify.Enum.Rol;
import org.example.meetify.Repositories.UsuarioRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final PasswordEncoder codificadorContrasenia;
    private final JWTService jwtService;

    public Usuario obtenerUsuarioPorCorreo(String correoElectronico) {
        Optional<Usuario> usuario = usuarioRepository.findTopByCorreoElectronico(correoElectronico);
        return usuario.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ese correo"));
    }

    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findTopByNombreUsuario(nombreUsuario);
        return usuario.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ese nombre de usuario"));
    }


    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        return usuarioRepository.findTopByNombreUsuario(nombreUsuario).orElse(null);
    }

    @Transactional
    public Usuario registrarUsuario(RegistroDTO dto) {
        if (usuarioRepository.findTopByNombreUsuario(dto.getNombreUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        if (usuarioRepository.findTopByCorreoElectronico(dto.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(dto.getNombreUsuario());
        nuevoUsuario.setContrasenia(codificadorContrasenia.encode(dto.getContrasenia()));
        nuevoUsuario.setCorreoElectronico(dto.getCorreoElectronico());
        nuevoUsuario.setRol(Rol.PERFIL);

        Perfil perfil = new Perfil();
        perfil.setNombre(dto.getNombre());
        perfil.setApellidos(dto.getApellidos());
        perfil.setCorreoElectronico(dto.getCorreoElectronico());

        // FECHA NACIMIENTO (STRING) -> LOCALDATE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento(), formatter);
        perfil.setFechaNacimiento(fechaNacimiento);

        try {
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            perfil.setUsuario(usuarioGuardado);
            perfilService.guardarPerfil(perfil);
            return usuarioGuardado;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage());
        }
    }

    public ResponseEntity<RespuestaDTO> iniciarSesion(LoginDTO dto) {
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByNombreUsuario(dto.getNombreUsuario());

        if (!usuarioOpcional.isPresent()) {
            usuarioOpcional = usuarioRepository.findTopByCorreoElectronico(dto.getNombreUsuario());
        }

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            if (codificadorContrasenia.matches(dto.getContrasenia(), usuario.getPassword())) {
                String token = jwtService.generateToken(usuario);
                return ResponseEntity
                        .ok(RespuestaDTO
                                .builder()
                                .estado(HttpStatus.OK.value())
                                .token(token).build());
            } else {
                throw new BadCredentialsException("Contraseña incorrecta");
            }
        } else {
            throw new UsernameNotFoundException("Usuario no encontrado");
        }
    }
}