package org.example.meetify.Repositories;

import org.example.meetify.models.Perfil;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.PerfilCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerfilCategoriaRepository extends JpaRepository<PerfilCategoria, Integer> {
    List<PerfilCategoria> findByPerfil(Perfil perfil);
    List<PerfilCategoria> findByCategoria(Categoria categoria);
    void delete(PerfilCategoria perfilCategoria);
}
