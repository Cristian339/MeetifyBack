package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.CategoriaDTO;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.Services.*;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.PerfilCategoria;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
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
    private PerfilCategoriaService perfilCategoriaService;

    @GetMapping("/mi")
    public PerfilDTO obtenerMiPerfil(@RequestHeader("Authorization") String token) {
        Perfil perfilLogueado = jwtService.extraerPerfilToken(token);
        return perfilService.getPerfilDTOPorId(perfilLogueado.getId());
    }

    @PostMapping("/actualizar-categorias/{correo}")
    public void cambiarCategorias(@RequestBody List<String> categorias,@PathVariable String correo) {
        publicacionService.cambiarCategoriaPerfil(categorias,correo);
    }


    @GetMapping("/compartidos")
    public List<PublicacionDTO> obtenerPublicacionesCompartidas() {
        return compartirService.publicacionesCompartidas();
    }

    // Permitir que un perfil comparta una publicación
    @PostMapping("/compartir/{id}")
    public PublicacionDTO compartirPublicacion(@PathVariable Integer id, @RequestHeader("Authorization") String token) {
        Perfil perfil = jwtService.extraerPerfilToken(token);
        System.out.println(perfil.getCorreoElectronico());
        return compartirService.compartirPublicacion(id,perfil);
    }


    @GetMapping("/categorias")
    public List<CategoriaDTO> obtenerCategorias() {

        return perfilCategoriaService.obtenerCategoriasPorPerfil2();
    }


}
