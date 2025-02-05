package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.Services.CompartirService;
import org.example.meetify.Services.PerfilCategoriaService;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.Services.PublicacionService;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Publicacion;
import org.example.meetify.seguridad.JWTService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/publicacion/perfil")
@AllArgsConstructor
public class PerfilController {
    private PerfilService perfilService;
    private JWTService jwtService;
    private PublicacionService publicacionService;
    private CompartirService compartirService;

    @GetMapping("/mi")
    public PerfilDTO obtenerMiPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilDTOPorId(perfilLogueado.getId());
    }

    @PostMapping("/actualizar-categorias")
    public void cambiarCategorias(@RequestBody List<String> categorias) {
        publicacionService.cambiarCategoriaPerfil(categorias);
    }


    @GetMapping("/compartidos")
    public List<PublicacionDTO> obtenerPublicacionesCompartidas() {
        return compartirService.publicacionesCompartidas();
    }

    // Permitir que un perfil comparta una publicación
    @PostMapping("/compartir/{id}")
    public PublicacionDTO compartirPublicacion(@PathVariable Integer id) {
        return compartirService.compartirPublicacion(id);
    }



}
