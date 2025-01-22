package org.example.meetify.Mappers;

import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.models.Perfil;
import org.mapstruct.Mapper;


import java.util.List;

@Mapper(componentModel = "spring")
public interface PerfilMapper {

    Perfil toEntity(PerfilDTO dto);

    /**
     * Este método sirve para pasar a DTO un Entity de Perfil
     * @param entity
     * @return
     */
    PerfilDTO toDTO(Perfil entity);

    List<Perfil> toEntity(List<PerfilDTO> dtos);

    List<PerfilDTO> toDTO(List<Perfil> entities);
}