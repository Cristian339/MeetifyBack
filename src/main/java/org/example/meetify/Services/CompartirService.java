package org.example.meetify.Services;

import lombok.RequiredArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.Repositories.CompartirRepository;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.models.Compartir;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompartirService {

    @Autowired
    private CompartirRepository compartirRepository;

    @Autowired
    private PerfilService perfilService;

    @Autowired
    private PublicacionService publicacionService;

    @Autowired
    private JWTFilter jwtFilter;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PerfilRepository perfilRepository;

    // Permitir a un usuario compartir una publicación
    public PublicacionDTO compartirPublicacion(int publicacionId, Perfil perfil) {

        Publicacion publicacion = publicacionService.encontrarPublicacionPorId(publicacionId);

        if (!obtenerPublicacionesCompartidasPorPerfil().contains(publicacion)) {
            Compartir compartir = new Compartir();
            compartir.setPerfil(perfil);
            compartir.setPublicacion(publicacion);
            compartirRepository.save(compartir);

            PublicacionDTO dto = new PublicacionDTO(publicacion.getId(), publicacion.getUsuarioCreador().getNombreUsuario(),
                    publicacion.getCategoria().getNombre(), publicacion.getImagenUrlPub(), publicacion.getImagenUrlPerfil(),
                    publicacion.getTitulo(), publicacion.getDescripcion(), publicacion.getUbicacion(),
                    publicacion.getFechaIni(), publicacion.getFechaFin());
            return dto;
        } else {
            throw new RuntimeException("Perfil o publicación no encontrados");
        }
    }


    public List<PublicacionDTO> obtenerPublicacionesCompartidasPorOtroPerfil(Integer id) {
        Perfil perfil = perfilRepository.findById(id).orElse(null);
        List<Publicacion> publicaciones = compartirRepository.findByPerfil_Id(perfil.getId())
                .stream()
                .map(Compartir::getPublicacion)
                .toList();


        List<PublicacionDTO> publicacionesDTO = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            PublicacionDTO dto = new PublicacionDTO(publicacion.getId(), publicacion.getUsuarioCreador().getNombreUsuario(),
                    publicacion.getCategoria().getNombre(), publicacion.getImagenUrlPub(), publicacion.getImagenUrlPerfil(),
                    publicacion.getTitulo(), publicacion.getDescripcion(), publicacion.getUbicacion(),
                    publicacion.getFechaIni(), publicacion.getFechaFin());

            // Añadir el DTO a la lista
            publicacionesDTO.add(dto);
        }

        return publicacionesDTO;
    }


    public List<Publicacion> obtenerPublicacionesCompartidasPorPerfil() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());
        return compartirRepository.findByPerfil_Id(perfil.getId())
                .stream()
                .map(Compartir::getPublicacion)
                .collect(Collectors.toList());
    }




    public List<PublicacionIdDTO> publicacionesCompartidas() {
        // Obtener las publicaciones compartidas por el perfil
        List<Publicacion> publicaciones = obtenerPublicacionesCompartidasPorPerfil();

        // Mapear cada Publicacion a PublicacionDTO
        List<PublicacionIdDTO> publicacionesDTO = new ArrayList<>();
        for (Publicacion publicacion : publicaciones) {
            Perfil perfil = perfilService.obtenerPerfilPorCorreo(publicacion.getUsuarioCreador().getCorreoElectronico());
            PublicacionIdDTO dto = new PublicacionIdDTO(publicacion.getId(), publicacion.getUsuarioCreador().getNombreUsuario(),
                    publicacion.getCategoria().getNombre(), publicacion.getImagenUrlPub(),perfil.getImagenUrlPerfil() ,
                    publicacion.getTitulo(), publicacion.getDescripcion(), publicacion.getUbicacion(),
                    publicacion.getFechaIni(), publicacion.getFechaFin());

            // Añadir el DTO a la lista
            publicacionesDTO.add(dto);
        }

        return publicacionesDTO;
    }

    public void eliminarTodasLasPublicacionesCompartidasPorPerfil(Perfil perfil) {
        // Obtenemos todas las publicaciones compartidas por este perfil
        List<Compartir> publicacionesCompartidas = compartirRepository.findByPerfil_Id(perfil.getId());

        // Si hay publicaciones compartidas, las eliminamos
        if (!publicacionesCompartidas.isEmpty()) {
            compartirRepository.deleteAll(publicacionesCompartidas);
        }
    }

}