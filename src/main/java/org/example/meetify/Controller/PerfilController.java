package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.ActualizarBiografiaDTO;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.models.Perfil;
import org.example.meetify.seguridad.JWTService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("publicacion/perfil")
@AllArgsConstructor
public class PerfilController {
    private PerfilService perfilService;
    private JWTService jwtService;


    @GetMapping("/mi")
    public PerfilDTO obtenerMiPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilDTOPorId(perfilLogueado.getId());
    }

}
