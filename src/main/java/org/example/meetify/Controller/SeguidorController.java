package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.AmigoDTO;
import org.example.meetify.DTO.SeguidorDTO;
import org.example.meetify.Services.SeguidoresService;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seguidores")
@AllArgsConstructor
public class SeguidorController {

    private final SeguidoresService seguidoresService;
    private final JWTService jwtService;

    @GetMapping("/seguidores")
    @PreAuthorize("isAuthenticated()")
    public List<SeguidorDTO> obtenerSeguidores() {
        return seguidoresService.obtenerSeguidores();
    }


    @GetMapping("/comprobar/{id}")
    public boolean comprobarSiSigues(@PathVariable Integer id) {
        return seguidoresService.seguirA(id);
    }


    @GetMapping("/seguidores-otro/{id}")
    public List<SeguidorDTO> obtenerSeguidoresOtro(@PathVariable Integer id) {
        return seguidoresService.obtenerSeguidoresOtro(id);
    }

    @GetMapping("/seguidos")
    @PreAuthorize("isAuthenticated()")
    public List<SeguidorDTO> obtenerSeguidos() {
        return seguidoresService.obtenerSeguidos();
    }

    @GetMapping("/seguidos-otro/{id}")
    public List<SeguidorDTO> obtenerSeguidosOtro(@PathVariable Integer id) {
        return seguidoresService.obtenerSeguidosOtro(id);
    }

    @PostMapping("/seguir/{idUsuarioASeguir}")
    @PreAuthorize("isAuthenticated()")
    public void seguirUsuario(@PathVariable Integer idUsuarioASeguir) {
        seguidoresService.seguirUsuario(idUsuarioASeguir);
    }

    @PostMapping("/dejar-de-seguir/{idUsuarioADejarDeSeguir}")
    @PreAuthorize("isAuthenticated()")
    public void dejarDeSeguirUsuario(@PathVariable Integer idUsuarioADejarDeSeguir) {
        seguidoresService.dejarDeSeguirUsuario(idUsuarioADejarDeSeguir);
    }


    @GetMapping("/amigos")
    public List<AmigoDTO> obtenerAmigos(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return seguidoresService.obtenerAmigos(perfilLogueado);
    }





    /*
    *     @GetMapping("/mi-id")
    public PerfilIDDTO obtenerMiPerfilId(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilIDDTOById(perfilLogueado.getId());
    }*/
}