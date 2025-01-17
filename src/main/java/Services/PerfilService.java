package Services;

import Entities.Perfil;
import Repositories.PerfilRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@AllArgsConstructor
@Validated
public class PerfilService {
    private PerfilRepository perfilRepository;


    public Perfil guardarPerfil(Perfil perfil){
        return perfilRepository.save(perfil);
    }
}
