package Services;


import Entities.Perfil;
import Entities.Usuario;

import Repositories.UsuarioRepository;
import Security.JWTService;
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
import java.util.HashSet;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {


    private UsuarioRepository usuarioRepository;
    private  perfilService;
    private final PasswordEncoder passwordEncoder;
    private JWTService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return usuarioRepository.findTopByUsername(username).orElse(null);
    }


    public Usuario registrarUsuario(RegistroDTO dto){

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername(dto.getUsername());
        nuevoUsuario.setPassword(passwordEncoder.encode(dto.getPassword()));
        nuevoUsuario.setRol(Rol.PERFIL);


        Perfil perfil = new Perfil();
        perfil.setNombre(dto.getNombre());
        perfil.setApellidos(dto.getApellidos());
        perfil.setDni(dto.getDni());
        perfil.setMail(dto.getMail());
        perfil.setPuesto("Sin Puesto");
        perfil.setAptitudes(new HashSet<>());
        perfil.setPublicacion(new HashSet<>());


        //FECHA NACIMIENTO (STRING) -> LOCALTADE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento(), formatter);
        perfil.setFechaNacimiento(fechaNacimiento);

        Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);


        perfil.setUsuario(usuarioGuardado);
        Perfil perfilGuardado = perfilService.guardarPerfil(perfil);


        return usuarioGuardado;
    }


    public ResponseEntity<RespuestaDTO> login(LoginDTO dto){

        // Buscar usuario por nombre de usuario
        Optional<Usuario> usuarioOpcional = usuarioRepository.findTopByUsername(dto.getUsername());

        if (usuarioOpcional.isPresent()) {
            Usuario usuario = usuarioOpcional.get();

            // Verificar la contraseña
            if (passwordEncoder.matches(dto.getPassword(), usuario.getPassword())) {

                // Contraseña válida, devolver token de acceso
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












//
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;
//    }
//
//    public Usuario buscarUsuarioPorNombre(String username){
//        return usuarioRepository.findTopByUsername(username).orElse(null);
//    }
//
//
//
//    public Usuario guardarUsuario(UsuarioDTO dto){
//
//        Usuario usuario = new Usuario();
//        usuario.setUsername(dto.getUsername());
//        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));
//        usuario.setRol(Rol.PERFIL);
//        return usuarioRepository.save(usuario);
//
//    }


}
