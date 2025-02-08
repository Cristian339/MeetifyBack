package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.DTO.PublicacionIdDTO;
import org.example.meetify.Repositories.PublicacionRepository;
import org.example.meetify.Services.CompartirService;
import org.example.meetify.Services.PublicacionService;
import org.example.meetify.models.Publicacion;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

    private PublicacionService service;

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
    public String eliminarPublicacion(@PathVariable Integer idPub) {
        return service.eliminarPublicacion(idPub);
    }


}
