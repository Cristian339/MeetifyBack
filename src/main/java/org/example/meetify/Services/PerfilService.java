package org.example.meetify.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.meetify.DTO.ActualizarBiografiaDTO;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.Enum.Rol;
import org.example.meetify.Mappers.PerfilMapper;
import org.example.meetify.Repositories.CategoriaRepository;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.Repositories.SeguidoresRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Seguidores;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.example.meetify.seguridad.JWTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class PerfilService {

    private PerfilRepository perfilRepository;

    private PerfilMapper perfilMapper;

    private SeguidoresRepository seguidoresRepository;

    public Perfil guardarPerfil(Perfil perfil){
        return perfilRepository.save(perfil);
    }



    public List<PerfilDTO> getAll(){

        List<Perfil> perfiles = perfilRepository.findAll();
        List<PerfilDTO> perfilDTOS = new ArrayList<>();

        for(Perfil p : perfiles){
            PerfilDTO dto = new PerfilDTO();
            dto.setNombre(p.getNombre());
            dto.setApellidos(p.getApellidos());
            dto.setCorreoElectronico(p.getCorreoElectronico());
            dto.setGenero(p.getGenero());
            dto.setBiografia(p.getBiografia());
            dto.setPais(p.getPais());
            dto.setPuntajeTotal(p.getPuntajeTotal());

            perfilDTOS.add(dto);
        }

        return perfilDTOS;
    }

    public Perfil getById(Integer id){
        return perfilRepository.findById(id).orElse(null);
    }

    public String eliminar(Integer id){
        String mensaje;
        Perfil perfil = getById(id);

        if(perfil == null){
            return  "Ese perfil no existe";
        }

        try {
            perfilRepository.deleteById(id);
            perfil = getById(id);
            if(perfil != null){
                mensaje =  "No se pudo eliminar el perfil";
            }else{
                mensaje = "Perfil eliminado correctamente";
            }
        } catch (Exception e) {
            mensaje =  "No se pudo eliminar el perfil";
        }

        return mensaje;
    }



    public Perfil obtenerPerfilPorCorreo(String correoElectronico) {
        Optional<Perfil> perfil = perfilRepository.findByCorreoElectronico(correoElectronico);
        return perfil.orElseThrow(() -> new RuntimeException("Perfil no encontrado"));
    }

    public void eliminar(Perfil perfil){
        perfilRepository.delete(perfil);
    }

    public PerfilDTO getPerfilDTOPorId(Integer id){
        Perfil perfil = getById(id);
        return perfilMapper.toDTO(perfil);
    }

    @Transactional
    public PerfilDTO actualizarBiografia(Integer id, ActualizarBiografiaDTO dto) {
        Logger logger = LoggerFactory.getLogger(PerfilService.class);
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Perfil no encontrado con id: {}", id);
                    return new IllegalArgumentException("Perfil no encontrado con id: " + id);
                });

        if (dto.getPais() != null) perfil.setPais(dto.getPais());
        if (dto.getGenero() != null) {
            perfil.setGenero(dto.getGenero());
        }
        if (dto.getImagenUrlPerfil() != null) perfil.setImagenUrlPerfil(dto.getImagenUrlPerfil());
        if (dto.getBiografia() != null) perfil.setBiografia(dto.getBiografia());

        Perfil updatedPerfil = perfilRepository.save(perfil);
        logger.info("Perfil actualizado con id: {}", id);
        return perfilMapper.toDTO(updatedPerfil);
    }

    public Perfil buscarPorUsuario(Usuario usuario){
        return perfilRepository.findTopByUsuario(usuario);
    }


    public List<PerfilDTO> listaDeBaneados(){
        List<PerfilDTO> baneados = new ArrayList<>();
        List<Perfil> todos = perfilRepository.findAll();

        for (Perfil p : todos){
            if(p.getBaneado() && p.getUsuario().getRol() == Rol.PERFIL){
                PerfilDTO dto = new PerfilDTO();
                dto.setNombre(p.getNombre());
                dto.setApellidos(p.getApellidos());
                dto.setCorreoElectronico(p.getCorreoElectronico());
                dto.setNombreUsuario(p.getNombre());
                baneados.add(dto);
            }
        }
        return baneados;
    }

    public List<PerfilDTO> listaDeNoBaneados(){
        List<PerfilDTO> baneados = new ArrayList<>();
        List<Perfil> todos = perfilRepository.findAll();

        for (Perfil p : todos){
            if(!p.getBaneado() && p.getUsuario().getRol() == Rol.PERFIL){
                PerfilDTO dto = perfilMapper.toDTO(p);
                baneados.add(dto);
            }
        }
        return baneados;
    }

    public void ban(String correo){
        Perfil perfil = obtenerPerfilPorCorreo(correo);

        if(perfil.getBaneado()){
            perfil.setBaneado(false);
        }else{
            perfil.setBaneado(true);
        }

        perfilRepository.save(perfil);
    }


    public void eliminarPerfil(Perfil perfil){
        List<Seguidores> seg = perfil.getSeguidores();
        seguidoresRepository.deleteAll(perfil.getSeguidores());
        perfilRepository.delete(perfil);
    }

    public PerfilDTO actualizarPerfil(Integer id, PerfilDTO perfilDTO) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Perfil no encontrado con id: " + id));

        perfil.setNombre(perfilDTO.getNombre());
        perfil.setApellidos(perfilDTO.getApellidos());
        perfil.setCorreoElectronico(perfilDTO.getCorreoElectronico());
        perfil.setGenero(perfilDTO.getGenero());
        perfil.setBiografia(perfilDTO.getBiografia());
        perfil.setPais(perfilDTO.getPais());
        perfil.setImagenUrlPerfil(perfilDTO.getImagenUrlPerfil());

        Perfil updatedPerfil = perfilRepository.save(perfil);
        return perfilMapper.toDTO(updatedPerfil);
    }
}

