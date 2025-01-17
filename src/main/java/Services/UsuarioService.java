package Services;

import Entities.Perfil;
import Entities.Usuario;
import Enum.Rol;
import Repositories.UsuarioRepository;
import Security.JWTService;
import dto.LoginDTO;
import dto.RegistroDTO;
import dto.RespuestaDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;
    private final PerfilService perfilService;
    private final PasswordEncoder codificadorContraseña;
    private final JWTService jwtService;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        return usuarioRepository.findTopByUsername(nombreUsuario).orElse(null);
    }

    public Usuario registrarUsuario(RegistroDTO dto) {
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setNombreUsuario(dto.getNombreUsuario());
        nuevoUsuario.setContraseña(codificadorContraseña.encode(dto.getContrasenia()));
        nuevoUsuario.setRol(Rol.PERFIL);

        Perfil perfil = new Perfil();
        perfil.setNombre(dto.getNombre());
        perfil.setApellidos(dto.getApellidos());
        perfil.setCorreoElectronico(dto.getCorreoElectronico());
        perfil.setPuntajeTotal(0);
        perfil.setPrivado(false);
        perfil.setBaneado(false);

        // FECHA NACIMIENTO (STRING) -> LOCALDATE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento(), formatter);
        perfil.setFechaNacimiento(fechaNacimiento);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
        perfil.setUsuario(usuarioGuardado);
        perfilService.guardarPerfil(perfil);

        return usuarioGuardado;
    }

    public ResponseEntity<RespuestaDTO> iniciarSesion(LoginDTO dto) {
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByUsername(dto.getUsername());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            if (codificadorContraseña.matches(dto.getPassword(), usuario.getPassword())) {
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