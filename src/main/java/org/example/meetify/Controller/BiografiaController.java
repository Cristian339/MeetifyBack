package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.ActualizarBiografiaDTO;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.models.Perfil;
import org.example.meetify.seguridad.JWTService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/biografia")
@AllArgsConstructor
public class BiografiaController {
private PerfilService perfilService;
private JWTService jwtService;

    @PostMapping("/actualizar/{correo}")
    public PerfilDTO actualizarBiografia(@RequestBody ActualizarBiografiaDTO actualizarBiografiaDTO,@PathVariable String correo) {
        Perfil perfilLogueado = perfilService.obtenerPerfilPorCorreo(correo);
        return perfilService.actualizarBiografia(perfilLogueado.getId(), actualizarBiografiaDTO);
    }

}
