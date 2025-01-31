package org.example.meetify.Services;

import org.example.meetify.Repositories.PerfilCategoriaRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.PerfilCategoria;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilCategoriaService {

    @Autowired
    private PerfilCategoriaRepository repository;





    public List<Categoria> obtenerCategoriasPorPerfil(Perfil perfil) {
        List<PerfilCategoria> usuarioCategorias = repository.findByPerfil(perfil);
        return usuarioCategorias.stream()
                .map(PerfilCategoria::getCategoria)
                .collect(Collectors.toList());
    }

    public PerfilCategoria anadirCategoriaAPerfil(Perfil perfil, Categoria categoria) {
        // Comprobar si ya existe la relación
        List<PerfilCategoria> existingRelations = repository.findByPerfil(perfil);
        boolean categoriaExists = existingRelations.stream()
                .anyMatch(pc -> pc.getCategoria().equals(categoria));

        if (categoriaExists) {
            throw new IllegalArgumentException("El perfil ya tiene esta categoría.");
        }

        // Crear la nueva relación
        PerfilCategoria perfilCategoria = new PerfilCategoria();
        perfilCategoria.setPerfil(perfil);
        perfilCategoria.setCategoria(categoria);

        // Guardar la nueva relación
        return repository.save(perfilCategoria);
    }


}
