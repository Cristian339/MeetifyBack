package org.example.meetify.Services;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.AmigoDTO;
import org.example.meetify.DTO.SeguidorDTO;
import org.example.meetify.Repositories.SeguidoresRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.Enum.Estado;
import org.example.meetify.models.Seguidores;
import org.example.meetify.seguridad.JWTFilter;
import org.example.meetify.Repositories.PerfilRepository;
import org.example.meetify.models.Usuario;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SeguidoresService {

    private final PerfilRepository perfilRepository;
    private final SeguidoresRepository seguidoresRepository;
    private final JWTFilter jwtFilter;
    private final UsuarioService usuarioService;

    public List<SeguidorDTO> obtenerSeguidores() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nadie te sigue"));

        return perfil.getSeguidores().stream()
                .map(seguidoresRel -> new SeguidorDTO(seguidoresRel.getSeguidor().getId(), seguidoresRel.getSeguidor().getNombre(), Estado.SEGUIDOR, seguidoresRel.getSeguido().getImagenUrlPerfil()))
                .collect(Collectors.toList());
    }


    public List<SeguidorDTO> obtenerSeguidoresOtro(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nadie te sigue"));

        return perfil.getSeguidores().stream()
                .map(seguidoresRel -> new SeguidorDTO(seguidoresRel.getSeguidor().getId(), seguidoresRel.getSeguidor().getNombre(), Estado.SEGUIDOR,seguidoresRel.getSeguido().getImagenUrlPerfil()))
                .collect(Collectors.toList());
    }

    public List<SeguidorDTO> obtenerSeguidos() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No sigues a nadie"));

        return perfil.getSeguidos().stream()
                .map(seguidoresRel -> new SeguidorDTO(seguidoresRel.getSeguido().getId(), seguidoresRel.getSeguido().getNombre(), Estado.SEGUIDO, seguidoresRel.getSeguido().getImagenUrlPerfil()))
                .collect(Collectors.toList());
    }


    public List<SeguidorDTO> obtenerSeguidosOtro(Integer id) {
        Perfil perfil = perfilRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No sigues a nadie"));

        return perfil.getSeguidos().stream()
                .map(seguidoresRel -> new SeguidorDTO(seguidoresRel.getSeguido().getId(), seguidoresRel.getSeguido().getNombre(), Estado.SEGUIDO,seguidoresRel.getSeguido().getImagenUrlPerfil()))
                .collect(Collectors.toList());
    }



    public void seguirUsuario(Integer idUsuarioASeguir) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        Perfil perfilASeguir = perfilRepository.findById(idUsuarioASeguir)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil a seguir no encontrado"));

        if (perfil.getId().equals(perfilASeguir.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes seguirte a ti mismo");
        }

        boolean yaSigue = seguidoresRepository.existsBySeguidorAndSeguido(perfil, perfilASeguir);
        if (yaSigue) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya sigues a este usuario");
        }

        Seguidores seguidores = new Seguidores();
        seguidores.setSeguidor(perfil);
        seguidores.setSeguido(perfilASeguir);
        boolean esMutuo = perfilASeguir.getSeguidores().stream().anyMatch(s -> s.getSeguidor().equals(perfil));
        seguidores.setEstado(esMutuo ? Estado.AMIGO : Estado.SEGUIDO);

        seguidoresRepository.save(seguidores);

        if (esMutuo) {
            Seguidores relacionMutua = seguidoresRepository.findBySeguidorAndSeguido(perfilASeguir, perfil);
            relacionMutua.setEstado(Estado.AMIGO);
            seguidoresRepository.save(relacionMutua);
        }
    }

    public void dejarDeSeguirUsuario(Integer idUsuarioADejarDeSeguir) {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        Perfil perfilADejarDeSeguir = perfilRepository.findById(idUsuarioADejarDeSeguir)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil a dejar de seguir no encontrado"));

        if (perfil.getId().equals(perfilADejarDeSeguir.getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes dejar de seguirte a ti mismo");
        }

        Seguidores seguidores = seguidoresRepository.findBySeguidorAndSeguido(perfil, perfilADejarDeSeguir);
        if (seguidores == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No sigues a este usuario");
        }

        seguidoresRepository.delete(seguidores);

        // Verificar si la relación era mutua y actualizar el estado del otro seguidor
        Seguidores relacionMutua = seguidoresRepository.findBySeguidorAndSeguido(perfilADejarDeSeguir, perfil);
        if (relacionMutua != null && relacionMutua.getEstado() == Estado.AMIGO) {
            relacionMutua.setEstado(Estado.SEGUIDO);
            seguidoresRepository.save(relacionMutua);
        }
    }


    public boolean seguirA(Integer idPerfilASeguir) {
        // Obtener el perfil del usuario autenticado
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        Usuario usuario = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilRepository.findById(usuario.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        // Obtener el perfil del usuario al que se quiere comprobar si se sigue
        Perfil perfilASeguir = perfilRepository.findById(idPerfilASeguir)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Perfil no encontrado"));

        // Buscar si existe una relación de seguimiento
        Seguidores seguidores = seguidoresRepository.findBySeguidorAndSeguido(perfil, perfilASeguir);

        // Si existe la relación, retornar verdadero (es decir, el usuario sigue al otro)
        return seguidores != null;
    }


    public List<AmigoDTO> obtenerAmigos(Perfil perfilLogueado) {
        return perfilLogueado.getSeguidos().stream()
                .filter(seguidosRel -> seguidoresRepository.existsBySeguidorAndSeguido(seguidosRel.getSeguido(), perfilLogueado))
                .map(seguidosRel -> new AmigoDTO(seguidosRel.getSeguido().getId(), seguidosRel.getSeguido().getNombre(), seguidosRel.getSeguido().getApellidos(), seguidosRel.getSeguido().getImagenUrlPerfil()))
                .collect(Collectors.toList());
    }

}