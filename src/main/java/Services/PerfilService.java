package Services;

import Entities.Perfil;
import Repositories.PerfilRepository;
import dto.PerfilDTO;
import lombok.AllArgsConstructor;
import mappers.PerfilMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.List;

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

    public void eliminar(Perfil perfil){
        perfilRepository.delete(perfil);
    }
}
