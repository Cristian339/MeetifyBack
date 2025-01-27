package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.Services.PerfilService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/perfil")
@AllArgsConstructor
public class PerfilController {
    private PerfilService perfilService;

    @GetMapping("/{id}")
    public PerfilDTO obtenerPerfil(@PathVariable Integer id) {
        return perfilService.getPerfilDTOPorId(id);
    }
}
