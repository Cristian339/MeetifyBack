package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.SeguidorDTO;
import org.example.meetify.Services.SeguidoresService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/seguidores")
@AllArgsConstructor
public class SeguidorController {

    private final SeguidoresService seguidoresService;

    @GetMapping("/seguidores")
    @PreAuthorize("isAuthenticated()")
    public List<SeguidorDTO> obtenerSeguidores() {
        return seguidoresService.obtenerSeguidores();
    }


    @GetMapping("/comprobar/{id}")
    public boolean comprobarSiSigues(@PathVariable Integer id){
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

    @GetMapping("/amigos")
    @PreAuthorize("isAuthenticated()")
    public List<SeguidorDTO> obtenerAmigos() {
        return seguidoresService.obtenerAmigos();
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
}