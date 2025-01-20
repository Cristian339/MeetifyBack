package mappers;

import Entities.Perfil;
import dto.PerfilDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
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
