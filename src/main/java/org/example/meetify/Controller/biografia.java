package org.example.meetify.Controller;

import org.example.meetify.DTO.ActualizarBiografiaDTO;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.models.Perfil;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public class biografia {
private PerfilService perfilService;

/*    @PostMapping("/biografia")
    public PerfilDTO actualizarBiografia(@RequestHeader("Authorization") String token, @RequestBody ActualizarBiografiaDTO actualizarBiografiaDTO) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.actualizarBiografia(perfilLogueado.getId(), actualizarBiografiaDTO);
    }*/

}
