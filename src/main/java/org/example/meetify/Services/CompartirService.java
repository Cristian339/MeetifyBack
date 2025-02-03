package org.example.meetify.Services;

import lombok.RequiredArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.Repositories.CompartirRepository;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.models.Compartir;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompartirService {

    private final CompartirRepository compartirRepository;
    private final PerfilService perfilService;
    private final PublicacionService publicacionService;
    private final JWTFilter jwtFilter;
    private final UsuarioService usuarioService;

    // Permitir a un usuario compartir una publicación
    public PublicacionDTO compartirPublicacion(int publicacionId) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        System.out.println(correoAutenticado);
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());

        Publicacion publicacion = publicacionService.encontrarPublicacionPorId(publicacionId);


        if (!obtenerPublicacionesCompartidasPorPerfil().contains(publicacion)) {
            Compartir compartir = new Compartir();
            compartir.setPerfil(perfil);
            compartir.setPublicacion(publicacion);
            compartirRepository.save(compartir);
            PublicacionDTO dto = new PublicacionDTO(publicacion.getUsuarioCreador().getNombreUsuario(),
                    publicacion.getCategoria().getNombre(),publicacion.getImagenUrl(),publicacion.getTitulo(),
                    publicacion.getDescripcion(),publicacion.getUbicacion(),publicacion.getFechaIni(),publicacion.getFechaFin());
            return dto;
        } else {
            throw new RuntimeException("Perfil o publicación no encontrados");
        }
    }

    public List<Publicacion> obtenerPublicacionesCompartidasPorPerfil() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        System.out.println(correoAutenticado);
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());
        return compartirRepository.findByPerfil_Id(perfil.getId())
                .stream()
                .map(compartir -> compartir.getPublicacion())
                .collect(Collectors.toList());
    }

    public List<PublicacionDTO> publicacionesCompartidas() {
        // Obtener las publicaciones compartidas por el perfil
        List<Publicacion> publicaciones = obtenerPublicacionesCompartidasPorPerfil();

        // Mapear cada Publicacion a PublicacionDTO
        List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            PublicacionDTO dto = new PublicacionDTO();

            // Mapeo de los atributos de Publicacion a PublicacionDTO
            dto.setNombrePerfil(publicacion.getUsuarioCreador().getNombreUsuario());  // Suponiendo que `getPerfil()` devuelve el perfil de la publicación
            dto.setCategoria(publicacion.getCategoria().getNombre());
            dto.setImageUrl(publicacion.getImagenUrl());
            dto.setTitulo(publicacion.getTitulo());
            dto.setDescripcion(publicacion.getDescripcion());
            dto.setUbicacion(publicacion.getUbicacion());
            dto.setFechaIni(publicacion.getFechaIni());
            dto.setFechaFin(publicacion.getFechaFin());

            // Añadir el DTO a la lista
            publicacionesDTO.add(dto);
        }

        return publicacionesDTO;
    }

}



