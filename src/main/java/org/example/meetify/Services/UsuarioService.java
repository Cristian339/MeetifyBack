package org.example.meetify.Services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.DTO.PerfilconIdDTO;
import org.example.meetify.DTO.RegistroDTO;
import org.example.meetify.Enum.Rol;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.UsuarioRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.springframework.data.repository.support.Repositories;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UsuarioService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    private final PerfilService perfilService;
    private final PasswordEncoder codificadorContrasenia;
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final PerfilRepository perfilRepository;


    public List<PerfilconIdDTO> getAllPerfiles(Perfil perfil) {

        List<Perfil> perfiles = perfilRepository.findAll();
        List<PerfilconIdDTO> perfilDTOS = new ArrayList<>();

        for (Perfil p : perfiles) {
            if(!p.equals(perfil)) {
                Usuario usuario = obtenerUsuarioPorCorreo(p.getCorreoElectronico());
                PerfilconIdDTO dto = new PerfilconIdDTO();
                dto.setId(p.getId());
                dto.setNombre(p.getNombre());
                dto.setApellidos(p.getApellidos());
                dto.setCorreoElectronico(p.getCorreoElectronico());
                dto.setImagenUrlPerfil(p.getImagenUrlPerfil());
                dto.setGenero(p.getGenero());
                dto.setBiografia(p.getBiografia());
                dto.setPais(p.getPais());
                dto.setPuntajeTotal(p.getPuntajeTotal());
                dto.setNombreUsuario(usuario.getNombreUsuario());
                perfilDTOS.add(dto);
            }
        }

        return perfilDTOS;
    }



    public Usuario obtenerUsuarioPorNombre(String nombreUsuario) {
        Optional<Usuario> usuario = usuarioRepository.findTopByNombreUsuario(nombreUsuario);
        return usuario.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ese nombre de usuario"));
    }

    public Usuario obtenerUsuarioPorCorreo(String correo) {
        Optional<Usuario> usuario = usuarioRepository.findTopByCorreoElectronico(correo);
        return usuario.orElseThrow(() -> new RuntimeException("Usuario no encontrado con ese nombre de usuario"));
    }


    public boolean autentificarse(String contrasenia, Usuario usuario){
        if(codificadorContrasenia.matches(contrasenia,usuario.getPassword())){
            return true;
        }else {
            return false;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) throws UsernameNotFoundException {
        return usuarioRepository.findTopByNombreUsuario(nombreUsuario).orElse(null);
    }

    @Transactional
    public Usuario registrarUsuario(RegistroDTO dto) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findTopByCorreoElectronico(dto.getCorreoElectronico());
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            if (!usuario.isCuentaVerificada()) {
                throw new IllegalArgumentException("El correo electrónico no ha sido verificado");
            }
        }

        if (usuarioRepository.findTopByNombreUsuario(dto.getNombreUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
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
        perfil.setBaneado(false);
        perfil.setEntrada(false);

        // FECHA NACIMIENTO (STRING) -> LOCALDATE
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate fechaNacimiento = LocalDate.parse(dto.getFechaNacimiento(), formatter);
        perfil.setFechaNacimiento(fechaNacimiento);

        // Enviar el correo
        enviarCorreoRegistro(dto.getCorreoElectronico());

        try {
            Usuario usuarioGuardado = usuarioRepository.save(nuevoUsuario);
            perfil.setUsuario(usuarioGuardado);
            perfilService.guardarPerfil(perfil);
            return usuarioGuardado;
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario: " + e.getMessage());
        }
    }

    private void enviarCorreoRegistro(String correoElectronico) {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setTo(correoElectronico);
            helper.setSubject("Verificación de correo");

            Context context = new Context();
            context.setVariable("mensaje", "¡Tu registro ha sido exitoso! Por favor, verifica tu correo electrónico.");
            context.setVariable("correo", correoElectronico);

            String htmlContent = templateEngine.process("Email", context);
            helper.setText(htmlContent, true);

            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException("Error al enviar el correo: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar la plantilla: " + e.getMessage());
        }
    }


    public boolean verificarCorreo(String correoElectronico) {
        Optional<Usuario> usuarioExistente = usuarioRepository.findTopByCorreoElectronico(correoElectronico);
        if (usuarioExistente.isPresent()) {
            Usuario usuario = usuarioExistente.get();
            if (!usuario.isCuentaVerificada()) {
                usuario.setCuentaVerificada(true);
                usuarioRepository.save(usuario);
                return true;
            }
        } else {
            // Crear y guardar el nuevo usuario
            Usuario nuevoUsuario = new Usuario();
            nuevoUsuario.setCorreoElectronico(correoElectronico);
            nuevoUsuario.setCuentaVerificada(true);
            usuarioRepository.save(nuevoUsuario);
            return true;
        }
        return false;
    }

    @Transactional
    public void iniciarRegistroUsuario(RegistroDTO dto) {
        if (usuarioRepository.findTopByCorreoElectronico(dto.getCorreoElectronico()).isPresent()) {
            throw new IllegalArgumentException("El correo electrónico ya está en uso");
        }

        if (usuarioRepository.findTopByNombreUsuario(dto.getNombreUsuario()).isPresent()) {
            throw new IllegalArgumentException("El nombre de usuario ya está en uso");
        }

        // Enviar el correo de verificación
        enviarCorreoRegistro(dto.getCorreoElectronico());
    }

}