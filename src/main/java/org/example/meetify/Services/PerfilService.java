package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.Mappers.PerfilMapper;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.models.Perfil;
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

    public List<Perfil> buscar(String nombre, String apellidos, String correoElectronico) {
        return perfilRepository.findByNombreAndApellidosAndCorreoElectronico(nombre, apellidos, correoElectronico);
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
            mensaje =  "No se pudoeliminar el perfil";
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
}
