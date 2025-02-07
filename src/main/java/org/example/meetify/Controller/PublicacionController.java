package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.DTO.UsuarioDTO;
import org.example.meetify.Services.CompartirService;
import org.example.meetify.Services.PublicacionService;
import org.example.meetify.models.Publicacion;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

    private PublicacionService service;

    @GetMapping("/all")
    public List<PublicacionIdDTO> general(){
        return service.getAll();
    }


    @GetMapping("/segui")
    public List<PublicacionDTO> seguidos(){
        return service.getSeguidos();
    }


    @PostMapping("/crear")
    public PublicacionDTO guardar(@RequestBody PublicacionDTO publicacionDTO){
        return service.aniadirPublicacion(publicacionDTO);
    }

    @GetMapping("all/mi")
    public List<PublicacionDTO> getMyPublications(){
        return service.verMisPublicaciones();
    }

    @DeleteMapping("/{idPub}")
    public String eliminarPublicacion(@PathVariable Integer idPub) {
        return service.eliminarPublicacion(idPub);
    }

    @PutMapping("/{idPub}")
    public PublicacionDTO actualizarPublicacion(@PathVariable Integer idPub, @RequestBody PublicacionDTO publicacionDTO){
        return service.actualizarPublicacion(idPub, publicacionDTO);
    }

    @GetMapping("/{idPub}")
    public PublicacionDTO obtenerPublicacionPorId(@PathVariable Integer idPub) {
        return service.obtenerPublicacionPorId(idPub);
    }

    @PostMapping("/unirse/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public void unirsePublicacion(@PathVariable Integer idPublicacion) {
        service.unirsePublicacion(idPublicacion);
    }

    @GetMapping("/usuarios-unidos/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public List<UsuarioDTO> obtenerUsuariosUnidos(@PathVariable Integer idPublicacion) {
        return service.obtenerUsuariosUnidos(idPublicacion);
    }

}
