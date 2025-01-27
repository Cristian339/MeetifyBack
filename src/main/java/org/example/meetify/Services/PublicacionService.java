package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.Repositories.PublicacionRepository;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PublicacionService {
    private PublicacionRepository repository;


/*
    public List<PublicacionDTO> getAll(Perfil perfil){
        List<Publicacion> publicaciones = repository.findAll();
        List<PublicacionDTO> publicacionDTOS;

        for (Publicacion p : publicaciones){
            if(!Objects.equals(p.getUsuarioCreador().getCorreoElectronico(), perfil.getCorreoElectronico())){

            }
        }


    }
*/
}
