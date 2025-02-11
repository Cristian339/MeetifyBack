package org.example.meetify.Services;

import org.example.meetify.DTO.PuntuacionDTO;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Reputacion;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.Repositories.ReputacionRepository;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PuntuacionService {

    @Autowired
    private ReputacionRepository reputacionRepository;

    @Autowired
    private PublicacionRepository publicacionRepository;

    @Autowired
    private PerfilRepository perfilRepository;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private UsuarioService usuarioService;

    private static final Logger logger = LoggerFactory.getLogger(PuntuacionService.class);

    // Méthod para puntuar una publicación
    public void puntuarPublicacion(Integer idPublicacion, Integer estrellas) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfilPuntuador = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        if (estrellas < 1 || estrellas > 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La puntuación debe estar entre 1 y 5 estrellas");
        }

        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicacion no encontrada"));

        Usuario usuarioCreador = publicacion.getUsuarioCreador();
        if (usuarioCreador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario creador no encontrado");
        }

        Perfil perfilCreador = usuarioCreador.getPerfil();
        if (perfilCreador == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil del creador no encontrado");
        }

        logger.info("Perfil del creador: {}", perfilCreador);

        if (perfilCreador.equals(perfilPuntuador)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes puntuar tu propia publicación");
        }

        Reputacion reputacion = new Reputacion();
        reputacion.setPublicacion(publicacion);
        reputacion.setPerfil(perfilPuntuador);
        reputacion.setEstrellas(estrellas);

        reputacionRepository.save(reputacion);

        Integer puntosReputacion = estrellas * 10;
        perfilCreador.setPuntajeTotal(perfilCreador.getPuntajeTotal() + puntosReputacion);
        perfilRepository.save(perfilCreador);
    }

    // Méthod para obtener las puntuaciones de una publicación
    public List<PuntuacionDTO> obtenerPuntuacionesDePublicacion(Integer idPublicacion) {
        Publicacion publicacion = publicacionRepository.findById(idPublicacion)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Publicación no encontrada"));

        List<Reputacion> reputaciones = reputacionRepository.findByPublicacion(publicacion);

        if (reputaciones.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay puntuaciones para esta publicación");
        }

        return reputaciones.stream()
                .map(reputacion -> new PuntuacionDTO(
                        reputacion.getId(),
                        reputacion.getPerfil().getUsuario().getNombreUsuario(),
                        reputacion.getEstrellas()))
                .collect(Collectors.toList());
    }


    public Integer obtenerCreador(Integer id){
        Publicacion publicacion = publicacionRepository.findById(id).orElse(null);
        if (publicacion != null) {
            return publicacion.getUsuarioCreador().getId();
        }else {
            throw new RuntimeException("Esta publicacion no existe");
        }
    }
}