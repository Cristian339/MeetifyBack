package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.MiPuntuacionDTO;
import org.example.meetify.DTO.PuntuacionTotalDTO;
import org.example.meetify.DTO.ReseniasMiasDTO;
import org.example.meetify.Repositories.ReputacionRepository;
import org.example.meetify.models.Reputacion;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ReputacionService {

    private final ReputacionRepository reputacionRepository;
    private final PublicacionService publicacionService;
    private final PerfilService perfilService;

    @Transactional
    public Reputacion puntuarPublicacion(Integer publicacionId, Perfil perfil, Integer estrellas, String motivo) {
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
        reputacion.setMotivo(motivo != null ? motivo : "");

        return reputacionRepository.save(reputacion);
    }

    public List<Reputacion> obtenerReputacionesPorPublicacion(Integer publicacionId) {
        Publicacion publicacion = publicacionService.encontrarPublicacionPorId(publicacionId);
        return reputacionRepository.findByPublicacion(publicacion);
    }

    public void eliminarReputacionPublicacion(Publicacion publicacion){
        List<Reputacion> reputacions = obtenerReputacionesPorPublicacion(publicacion.getId());
        reputacionRepository.deleteAll(reputacions);
    }

    public List<MiPuntuacionDTO> miReputacion(Perfil perfil) {
        Usuario usuario = perfil.getUsuario();
        List<Publicacion> publicaciones = publicacionService.getAllByPerfil(usuario);

        return publicaciones.stream().map(publicacion -> {
            List<Reputacion> reputaciones = reputacionRepository.findByPublicacion(publicacion);
            int mediaEstrellas = reputaciones.stream()
                    .mapToInt(Reputacion::getEstrellas)
                    .average()
                    .orElse(0.0) > 0.5 ? 1 : 4;

            return new MiPuntuacionDTO(
                    perfil.getNombre(),
                    publicacion.getImagenUrlPub(),
                    perfil.getImagenUrlPerfil(),
                    (int) (mediaEstrellas * 5),
                    publicacion.getTitulo(),
                    publicacion.getCategoria()
            );
        }).collect(Collectors.toList());
    }

    public PuntuacionTotalDTO obtenerPuntuacionTotal(Perfil perfil) {
        Usuario usuario = perfil.getUsuario();
        List<Publicacion> publicaciones = publicacionService.getAllByPerfil(usuario);

        int puntuacionTotal = publicaciones.stream()
                .flatMap(publicacion -> reputacionRepository.findByPublicacion(publicacion).stream())
                .mapToInt(Reputacion::getEstrellas)
                .sum() * 5;

        PuntuacionTotalDTO puntuacionTotalDTO = new PuntuacionTotalDTO();
        puntuacionTotalDTO.setPuntuacionTotal(puntuacionTotal);
        return puntuacionTotalDTO;
    }

    public List<ReseniasMiasDTO> obtenerMisResenias(Perfil perfil) {
        List<Reputacion> reputaciones = reputacionRepository.findByPerfil(perfil);
        Usuario usuario = perfil.getUsuario();
        List<ReseniasMiasDTO> reseniasMiasDTOS = new ArrayList<>();
        for(Reputacion reputacion : reputaciones) {
            Publicacion publicacion = reputacion.getPublicacion();
            ReseniasMiasDTO r = new ReseniasMiasDTO(publicacion.getImagenUrlPub(),publicacion.getTitulo(),
                    usuario.getNombreUsuario(),perfil.getImagenUrlPerfil(),reputacion.getEstrellas(),reputacion.getMotivo());
            reseniasMiasDTOS.add(r);
        }
        return reseniasMiasDTOS;
    }


}