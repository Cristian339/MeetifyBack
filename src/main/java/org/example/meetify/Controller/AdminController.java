package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PerfilDTO;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.Services.PerfilService;
import org.example.meetify.Services.PublicacionService;
import org.example.meetify.Services.UsuarioService;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/admin")
@AllArgsConstructor
public class AdminController {

    private PerfilService perfilService;

    private UsuarioService usuarioService;

    private PublicacionService publicacionService;

    @GetMapping("/baneados")
    public List<PerfilDTO> obtenerPerfilesBaneados() {
        List<PerfilDTO> perfiles = perfilService.listaDeBaneados();
        for (PerfilDTO p : perfiles){
            Usuario us = usuarioService.obtenerUsuarioPorCorreo(p.getCorreoElectronico());
            p.setNombreUsuario(us.getNombreUsuario());
        }
        return perfiles;
    }

    @GetMapping("/nobaneados")
    public List<PerfilDTO> obtenerPerfilesNoBaneados() {
        List<PerfilDTO> perfiles = perfilService.listaDeNoBaneados();
        for (PerfilDTO p : perfiles){
            Usuario us = usuarioService.obtenerUsuarioPorCorreo(p.getCorreoElectronico());
            p.setNombreUsuario(us.getNombreUsuario());
        }
        return perfiles;
    }

    @PostMapping("/ban/{correo}")
    public void baneo(@PathVariable String correo) {
        perfilService.ban(correo);
    }


    @GetMapping("/publicaciones/{correo}")
    public List<PublicacionIdDTO> publicacionesPerfil(@PathVariable String correo){
        return publicacionService.publicacionesPerfil(correo);
    }



}
