package org.example.meetify.Services;

import org.example.meetify.DTO.CategoriaDTO;
import org.example.meetify.Repositories.PerfilCategoriaRepository;
import org.example.meetify.models.Perfil;
import org.example.meetify.models.Categoria;
import org.example.meetify.models.PerfilCategoria;
import org.example.meetify.models.Usuario;
import org.example.meetify.seguridad.JWTFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PerfilCategoriaService {

    @Autowired
    private PerfilCategoriaRepository repository;

    @Autowired
    private JWTFilter jwtFilter;
    @Autowired
    private PerfilService perfilService;
    @Autowired
    private UsuarioService usuarioService;


    public List<Categoria> obtenerCategoriasPorPerfil(Perfil perfil) {
        List<PerfilCategoria> usuarioCategorias = repository.findByPerfil(perfil);
        return usuarioCategorias.stream()
                .map(PerfilCategoria::getCategoria)
                .collect(Collectors.toList());
    }


    public List<CategoriaDTO> obtenerCategoriasPorPerfil2() {
        String correoAutenticado = jwtFilter.obtenerCorreoAutenticado();
        System.out.println(correoAutenticado);
        Usuario usu = usuarioService.obtenerUsuarioPorNombre(correoAutenticado);
        Perfil perfil = perfilService.obtenerPerfilPorCorreo(usu.getCorreoElectronico());
        List<Categoria> cats = obtenerCategoriasPorPerfil(perfil);
        List<CategoriaDTO> categorias =  new ArrayList<>();
        for(Categoria cat : cats) {
            System.out.println(cat.getNombre());
            CategoriaDTO catDTO = new CategoriaDTO();
            catDTO.setNombre(cat.getNombre());
            categorias.add(catDTO);
        }
        return categorias;
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

    public void eliminarTodasLasCategoriasPorPerfil(Perfil perfil) {
        // Obtenemos todas las categorías asociadas con este perfil
        List<PerfilCategoria> categoriasPerfil = repository.findByPerfil(perfil);

        // Si existen categorías asociadas, las eliminamos
        if (!categoriasPerfil.isEmpty()) {
            repository.deleteAll(categoriasPerfil);
        }
    }



}
