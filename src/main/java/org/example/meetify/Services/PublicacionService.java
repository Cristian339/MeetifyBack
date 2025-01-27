package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.models.Publicacion;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@AllArgsConstructor
public class PublicacionService {

    private PublicacionRepository repository;

    private JWTFilter jwtFilter;

    public List<PublicacionDTO> getAll() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        List<Publicacion> publicaciones = repository.findAll();
        List<PublicacionDTO> publicacionDTOS = new ArrayList<>();

        for (Publicacion p : publicaciones) {
            // Filtra las publicaciones para excluir las del usuario autenticado
            if (!p.getUsuarioCreador().getCorreoElectronico().equals(correoAutenticado)) {
                PublicacionDTO dto = new PublicacionDTO(p.getUsuarioCreador().getNombreUsuario(),
                        p.getCategoria().getNombre(), p.getImagenUrl(), p.getTitulo(),
                        p.getDescripcion(), p.getUbicacion());
                publicacionDTOS.add(dto);
            }
        }

        return publicacionDTOS;

    }


}
