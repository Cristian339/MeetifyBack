package org.example.meetify.Controller;

import lombok.AllArgsConstructor;
import org.example.meetify.DTO.PublicacionDTO;
import org.example.meetify.Services.PublicacionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/publicacion")
@AllArgsConstructor
public class PublicacionController {

    private PublicacionService service;



    @GetMapping("/all")
    public List<PublicacionDTO> general(){
        return service.getAll();
    }

    @GetMapping("/segui")
    public List<PublicacionDTO> seguidos(){
        return service.getSeguidos();
    }



}
