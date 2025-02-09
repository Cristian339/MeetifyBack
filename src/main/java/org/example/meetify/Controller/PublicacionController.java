package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.DTO.PuntuacionDTO;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.DTO.UsuarioDTO;
import org.example.meetify.Services.CompartirService;
import org.example.meetify.Services.PublicacionService;
import org.example.meetify.Services.PuntuacionService;
import org.example.meetify.models.Publicacion;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

    private PublicacionService service;

    private PuntuacionService puntuacionService;

    private PublicacionRepository repository;

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

    @PostMapping("del/{idPub}")
    public void eliminarPublicacion2(@PathVariable Integer idPub) {
        repository.deleteById(idPub);
    }

    @DeleteMapping("/{idPub}")
    public ResponseEntity<String> eliminarPublicacion(@PathVariable Integer idPub) {
        try {
            service.eliminarPublicacion(idPub);
            return ResponseEntity.ok("Publicación eliminada exitosamente");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno al eliminar la publicación");
        }
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

    @DeleteMapping("/salir/{idPublicacion}")
    @PreAuthorize("isAuthenticated()")
    public void salirPublicacion(@PathVariable Integer idPublicacion) {
        service.salirPublicacion(idPublicacion);
    }

    @PostMapping("/puntuacion/{idPublicacion}/{estrellas}")
    public void puntuarPublicacion(@PathVariable Integer idPublicacion, @PathVariable Integer estrellas) {
        puntuacionService.puntuarPublicacion(idPublicacion, estrellas);
    }

    @GetMapping("/puntuaciones/{idPublicacion}")
    public ResponseEntity<List<PuntuacionDTO>> obtenerPuntuacionesDePublicacion(@PathVariable Integer idPublicacion) {
        List<PuntuacionDTO> puntuaciones = puntuacionService.obtenerPuntuacionesDePublicacion(idPublicacion);
        return ResponseEntity.ok(puntuaciones);
    }


}
