package org.example.meetify.Services;

import org.example.meetify.Repositories.PerfilCategoriaRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.PerfilCategoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilCategoriaService {

    private final PerfilCategoriaRepository repository;

    public PerfilCategoriaService(PerfilCategoriaRepository repository) {
        this.repository = repository;
    }


    public List<Categoria> obtenerCategoriasPorPerfil(Perfil perfil) {
        List<PerfilCategoria> usuarioCategorias = repository.findByPerfil(perfil);
        return usuarioCategorias.stream()
                .map(PerfilCategoria::getCategoria)
                .collect(Collectors.toList());
    }
}
