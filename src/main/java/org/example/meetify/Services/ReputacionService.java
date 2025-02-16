package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.Repositories.ReputacionRepository;
import org.example.meetify.models.Reputacion;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Perfil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class ReputacionService {

    private final ReputacionRepository reputacionRepository;
    private final PublicacionService publicacionService;
    private final PerfilService perfilService;

    @Transactional
    public Reputacion puntuarPublicacion(Integer publicacionId, Perfil perfil, Integer estrellas) {
        if (estrellas < 1 || estrellas > 5) {
            throw new IllegalArgumentException("La puntuación debe estar entre 1 y 5 estrellas");
        }

        Publicacion publicacion = publicacionService.encontrarPublicacionPorId(publicacionId);

        if (!publicacionService.estaEnPublicacion(publicacionId, perfil)) {
            throw new IllegalArgumentException("El usuario no es miembro del evento y no puede puntuar la publicación");
        }

        if (reputacionRepository.existsByPublicacionAndPerfil(publicacion, perfil)) {
            throw new IllegalArgumentException("El usuario ya ha puntuado esta publicación anteriormente");
        }

        Reputacion reputacion = new Reputacion();
        reputacion.setPublicacion(publicacion);
        reputacion.setPerfil(perfil);
        reputacion.setEstrellas(estrellas);

        return reputacionRepository.save(reputacion);
    }


    public List<Reputacion> obtenerReputacionesPorPublicacion(Integer publicacionId) {
        Publicacion publicacion = publicacionService.encontrarPublicacionPorId(publicacionId);
        return reputacionRepository.findByPublicacion(publicacion);
    }
}